# AXBot 开发指南

## 前言

作为一个开发者，你可以遵循本指南的内容，在你的本地运行axbot项目的代码。我们使用docker compose的方式运行axbot项目所需的服务，例如Postgres，Redis等。因此，在正式打开你的IDE并运行项目前，请遵照下文的内容，提前准备好环境

## 开发前提

在正式打开代码前，请确保你的开发环境中已经拥有以下软件：

- Docker，见 https://docs.docker.com/desktop/
- 趁手的开发JAVA的IDE，例如 IDEA，见 https://www.jetbrains.com/idea/
- 趁手的开发Python的IDE，例如 PyCharm，见 https://www.jetbrains.com/pycharm/

同时，请确保安装了以下所需的开发环境：

- Java 17+，推荐使用 https://adoptium.net/temurin/releases/
- Python 3.11+，见 https://www.python.org/downloads/

## 启动项目

### 启动依赖服务

我们将axbot项目所依赖的其他服务一律统称为“依赖服务”。在axbot中，我们的所有依赖服务均可以以docker镜像的方式获取，如果你正确在你本地安装了docker，那么使用以下命令即可一键启动项目所依赖的全部服务：

```bash
cd deps
docker compose up -d
```

运行上述命令，docker将在你的本地启用以下的依赖服务：

- postgres数据库
- redis数据库
- cqhttp，一个对接qq的客户端
- rabbitmq队列服务

当然，如果你不愿意使用docker来启动依赖服务，又或者是你本地已有了相关的资源，请自行下载安装启用相关的依赖服务

### 启动core服务

为机器人提供核心逻辑的服务，我们称之为core服务。core服务除了对接依赖服务外，还对接了一些其他的平台，因此要启动完整功能的core服务需要额外的配置。

因此，如果你不想要一下子对接完整的功能，可以以最小配置启动，并在接下来的开发过程中逐步对接其他服务。

#### 最小配置启动

在使用IDEA的前提下，将项目的根目录设置为 `/core`，IDEA会自动识别该目录为gradle项目，并自动完成gradle的相关配置和依赖下载。

> **注意：**你可以取消掉 `/core/build.gradle.kts `中 `// maven(url = "https://maven.aliyun.com/repository/public/")` 的注解，来使用阿里云的镜像仓库来提高依赖的下载速度

在IDEA完成依赖下载后，你应该更改你的配置项完成本地环境的配置。如果你使用的是上述docker compose的方法部署的依赖服务，那你不需要更改配置项上的任何一项，所有对接依赖服务的配置均已进行了最小配置（除了机器人平台的对接和一些付费服务的对接，见下文）

在调整了合适的配置项后，你即可启动`/core/axbot-server/src/main/java/com/github/axiangcoding/axbot/AxbotServerApplication` 来运行项目

#### 对接机器人平台

##### kook

axbot 以webhook的形式和kook平台进行对接。如果你需要对接kook，你需要：

1. 使用隧穿软件，将本地应用端口暴露到公网中（这里推荐使用花生壳 https://hsk.oray.com/）
2. 到https://developer.kookapp.cn/app/index 创建一个应用，并在`机器人`选项卡中的`机器人连接模式`栏目选择`webhook`模式，然后在`Callback Url`中填入隧穿后的公网地址。

然后，你需要将`Token`,`Verify Token` 填入配置文件中，即下面描述的部分

```yaml
bot:
  kook:
    enabled: true
    bot-token: <token>
    verify-token: <verify token>
  bot-market:
    uuid:
```

然后重启core服务，即可完成kook平台的对接

##### cqhttp

axbot 使用http的方式和cqhttp进行对接。如果你需要对接cqhttp来开启qq的接入，你需要：

1. 正确配置cqhttp，并启用cqhttp服务的反向http

如果有配置了`secret`，请将该配置项填入以下部分

```yaml
bot:
  cqhttp:
    enabled: true
    base-url: http://localhost:5700/
    secret: <secret>
```

#### 对接七牛云审核

在本地开发中，不建议开启七牛云AI文本审核的功能，因为调用这个接口需要付费。你可以指定`enabled`为`false`关闭文本审核功能，又或者是需要开启七牛云审核时，填入七牛云的AK和SK

```yaml
bot:
  censor:
    enabled: false
    qiniu:
      access-token: <qiniu ak>
      secret-token: <qiniu sk>
```

### 启动crawler服务

crawler服务是为了获取被机器人防护保护的网站的代码做的一个工作节点，使用mq支持了消费组模式。

运行crawler十分简单，首先需要指定运行的配置项

```properties
PYTHONUNBUFFERED=1;
PIKA_HOST=localhost;
PIKA_PORT=5672;
PIKA_USER=user;
PIKA_PASS=password
```

然后进入目录 `/crawler`，运行`main.py`即可

## 报告问题

虽然我们已经对服务的本地开发进行了验证，但是我们无法保证我们的流程描述是完整和准确的。如果你在遵循本指南的过程中遇到任何问题，请提交issue并寻求帮助。我们会尽力解决您的问题。