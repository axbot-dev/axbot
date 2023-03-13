FROM python:3.11-alpine
WORKDIR /app
COPY requirements.txt ./
RUN #pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple
RUN pip install -r requirements.txt

RUN #sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories
RUN apk update && apk upgrade
RUN apk add chromium chromium-chromedriver unzip curl

COPY main.py ./
ENTRYPOINT ["python", "-u", "main.py"]
