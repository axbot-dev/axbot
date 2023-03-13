import os
import sys
from time import sleep

import redis as redis
import undetected_chromedriver as uc

queue_in = "crawler_mission"
queue_out = "crawler_result"


# demo to use redis stream
def main():
    redis_host = os.getenv("REDIS_HOST")
    redis_port = int(os.getenv("REDIS_PORT"))

    rc = redis.StrictRedis(host=redis_host, port=redis_port)

    last_id = 0
    print("start consuming")
    while True:
        print("waiting for message...")
        resp = rc.xread(streams={queue_in: last_id}, count=1, block=1000)
        if resp:
            options = uc.ChromeOptions()
            options.add_argument("--disable-gpu")
            options.add_argument('--no-sandbox')
            driver = uc.Chrome(version_main=111, options=options, headless=True,
                               driver_executable_path="/usr/bin/chromedriver")
            key, messages = resp[0]
            last_id, data = messages[0]
            print("redis id", last_id)
            url = data[b'url'].decode('utf-8')
            task_id = data[b'task_id'].decode('utf-8')
            print("task_id", task_id, " url", url)

            driver.get(url)
            sleep(12)
            page_source = driver.page_source
            print("page source code convert, add to queue")
            rc.xadd(queue_out, {'page_source': page_source,
                                'task_id': task_id})
            print("delete the message")
            rc.xdel(queue_in, last_id)
            driver.close()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os.close(0)
