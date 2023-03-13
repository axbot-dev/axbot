import os

import redis

if __name__ == '__main__':
    redis_host = os.getenv("REDIS_HOST")
    redis_port = int(os.getenv("REDIS_PORT"))

    rc = redis.StrictRedis(host=redis_host, port=redis_port)
    rc.xadd("crawler_mission", {'url': 'https://warthunder.com/zh/community/userinfo/?nick=Kirov_reportin'})