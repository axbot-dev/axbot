import json
import os
import sys
import time

from loguru import logger
import pika
import undetected_chromedriver as uc
from pika import PlainCredentials
from selenium.common import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

queue_in = "crawler_mission"
queue_out = "crawler_result"


def main():
    pika_host = os.getenv("PIKA_HOST")
    pika_port = int(os.getenv("PIKA_PORT"))
    pika_user = os.getenv("PIKA_USER")
    pika_pass = os.getenv("PIKA_PASS")
    credentials = PlainCredentials(pika_user, pika_pass)

    connection = pika.BlockingConnection(
        pika.ConnectionParameters(host=pika_host, port=pika_port, credentials=credentials))
    channel = connection.channel()

    channel.queue_declare(queue=queue_in, durable=True)
    channel.queue_declare(queue=queue_out, durable=True)

    def callback(ch, method, properties, body):
        logger.info(f"received a message from queue {queue_in}")
        logger.info("starting chromedriver")

        start_time = time.time()
        options = uc.ChromeOptions()
        options.add_argument("--disable-gpu")
        options.add_argument('--no-sandbox')
        # comment driver_executable_path when you test in local

        execute_path = os.getenv("DRIVER_EXECUTABLE_PATH")
        if execute_path is None:
            driver = uc.Chrome(version_main=113, options=options, headless=True)
        else:
            driver = uc.Chrome(version_main=113, options=options, headless=True,
                               driver_executable_path=execute_path)

        json_obj = json.loads(body)
        url = json_obj["url"]
        mission_id = json_obj["missionId"]
        xpath_condition = json_obj["xpathCondition"]
        logger.info(f"missionId is {mission_id}, url is {url}, xpath condition is {xpath_condition}")
        driver.get(url)

        wait = WebDriverWait(driver, 25, 2)
        try:
            wait.until(EC.presence_of_element_located((By.XPATH, xpath_condition)))
        except TimeoutException:
            logger.error("timeout when wait element")
            return
        finally:
            driver.close()
        page_source = driver.page_source
        time_usage = time.time() - start_time
        logger.info(f"get page_source success, use {time_usage} sec", )
        out_obj = {
            "missionId": mission_id,
            "pageSource": page_source,
            "timeUsage": time_usage
        }
        body = json.dumps(out_obj, ensure_ascii=False)
        channel.basic_publish(exchange='', routing_key=queue_out, body=body.encode('utf-8'))
        logger.info(f"send a message to queue {queue_out}")

        driver.close()

    logger.info(f"start consuming at queue {queue_in}")
    channel.basic_consume(queue=queue_in, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        logger.info('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os.close(0)
