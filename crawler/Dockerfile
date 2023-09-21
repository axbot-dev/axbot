FROM python:3.11-alpine
LABEL org.opencontainers.image.source=https://github.com/axbot-dev/axbot
LABEL org.opencontainers.image.description="axbot crawler"
LABEL org.opencontainers.image.licenses="GPL-3.0"
WORKDIR /app
RUN #sed -i 's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' /etc/apk/repositories
RUN apk update && apk upgrade
RUN apk add chromium chromium-chromedriver unzip curl

COPY requirements.txt ./
RUN #pip config set global.index-url https://pypi.tuna.tsinghua.edu.cn/simple
RUN pip install -r requirements.txt
ARG VERSION
ENV APP_VERSION=${VERSION}

COPY main.py ./
ENTRYPOINT ["python", "-u", "main.py"]
