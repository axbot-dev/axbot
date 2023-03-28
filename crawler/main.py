import json
import logging
import os
import sys
import time

import pika
import undetected_chromedriver as uc
from pika import PlainCredentials
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

queue_in = "crawler_mission"
queue_out = "crawler_result"

logging.basicConfig(level=logging.INFO)


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
        logging.info("received a message from queue %s", queue_in)
        logging.info("starting chromedriver")
        start_time = time.time()
        options = uc.ChromeOptions()
        options.add_argument("--disable-gpu")
        options.add_argument('--no-sandbox')
        # comment driver_executable_path when you test in local
        driver = uc.Chrome(version_main=111, options=options, headless=True,
                           driver_executable_path="/usr/bin/chromedriver")
        json_obj = json.loads(body)
        url = json_obj["url"]
        mission_id = json_obj["missionId"]
        xpath_condition = json_obj["xpathCondition"]
        logging.info("missionId is %s, url is %s, xpath condition is %s", mission_id, url, xpath_condition)
        driver.get(url)

        wait = WebDriverWait(driver, 15, 1)

        wait.until(EC.presence_of_element_located((By.XPATH, xpath_condition)))

        page_source = driver.page_source
        end_time = time.time()
        logging.info("get page_source success, use %.2f sec", end_time - start_time)

        out_obj = {
            "missionId": mission_id,
            "pageSource": page_source,
            "timeUsage": end_time - start_time
        }
        channel.basic_publish(exchange='', routing_key=queue_out, body=json.dumps(out_obj, ensure_ascii=False))
        logging.info("send message to queue %s", queue_out)
        driver.close()

    logging.info("start consuming at queue %s", queue_in)
    channel.basic_consume(queue=queue_in, on_message_callback=callback, auto_ack=True)

    channel.start_consuming()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os.close(0)
