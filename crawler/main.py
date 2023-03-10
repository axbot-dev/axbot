import os
import sys
from time import sleep

import redis as redis
import undetected_chromedriver as uc

queue_in = "crawler_mission"
queue_out = "crawler_result"

driver = uc.Chrome(headless=True)

# demo to use redis stream
def main():
    redis_host = os.getenv("REDIS_HOST")
    redis_port = int(os.getenv("REDIS_PORT"))

    rc = redis.StrictRedis(host=redis_host, port=redis_port)
    last_id = 0
    print("start consuming")
    while True:
        resp = rc.xread(streams={queue_in: last_id}, count=1, block=1000)
        if resp:
            key, messages = resp[0]
            last_id, data = messages[0]
            print("REDIS ID: ", last_id)
            print(data[b'url'].decode('utf-8'))

            driver.get(data[b'url'].decode('utf-8'))
            sleep(12)
            page_source = driver.page_source
            print("page source code convert, add to queue")
            rc.xadd(queue_out, {'page_source': page_source})
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
    finally:
        driver.quit()

