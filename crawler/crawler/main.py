import json
import os
import sys
import time

import pika
from loguru import logger
from pika import PlainCredentials

from crawler import driver

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
        start_time = time.time()
        json_obj = json.loads(body)
        url = json_obj["url"]
        mission_id = json_obj["missionId"]
        xpath_condition = json_obj["xpathCondition"]

        page_source = driver.get_page_html(url, xpath_condition)
        if page_source == "":
            logger.error("get page_source failed")
            return
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

    logger.info(f"start consuming at queue {queue_in}")
    channel.basic_consume(queue=queue_in, on_message_callback=callback, auto_ack=True)
    channel.start_consuming()


if __name__ == '__main__':
    try:
        driver.config_browser_path(r'/usr/bin/chromium-browser')
        main()
    except KeyboardInterrupt:
        logger.info('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os.close(0)
