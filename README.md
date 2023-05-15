<p align="center">
 <img src="docs/axbot_logo.png" height="200px" />   
</p>

# AXBot 艾克斯机器人

[![GitHub](https://img.shields.io/github/license/axiangcoding/AXBot)](LICENSE) [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/axiangcoding/axbot-core?label=axbot-core&sort=semver)](https://hub.docker.com/r/axiangcoding/axbot-core) [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/axiangcoding/axbot-core?label=axbot-crawler&sort=semver)](https://hub.docker.com/r/axiangcoding/axbot-crawler) [![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/axiangcoding/AXBot/build_docker_image.yml?label=build_docker_image)](https://github.com/axiangcoding/AXBot/actions/workflows/build_docker_image.yml) [![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/axiangcoding/AXBot/codeql.yml?label=codeql)](https://github.com/axiangcoding/AXBot/actions/workflows/codeql.yml) [![Codacy grade](https://img.shields.io/codacy/grade/501ee223d049451d9de502036fab1ce1)](https://app.codacy.com/gh/axiangcoding/AXBot/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

## 简介

AXBot，全称艾克斯机器人（以下简称 "AXBot"），是一个能够在Kook、QQ等社交平台上使用的机器人。她可以做到很多事情，具备一些有趣的交互功能，但她更专注于游戏类功能的实现。AXBot的终极目标是成为一个功能丰富的游戏类问答机器人，能够协助玩家获取游戏数据、分析游戏数据、智能化回答游戏攻略，连接社交平台和游戏。

AXBot目前拥有以下有趣的小功能：

- 接入KOOK平台，Cqhttp平台
- 电子游戏 “战争雷霆” 的战绩查询
- 电子游戏 “战争雷霆” 的新闻播报
- B站直播间开播提醒
- 和ChatGPT聊天
- 每日电子算命

AXBot的设计思路来源于 [https://github.com/axiangcoding/antonstar-bot](https://github.com/axiangcoding/antonstar-bot)，asbot因为种种原因达到了其生命周期，AXBot是对asbot的延续和扩展。

走过路过，点个`star`吧

> 艾克斯机器人的中文名是音译而来的，而 "AX" 则是作者平时所使用的昵称，并无特殊含义

## 使用手册
### 普通用户

如果您是一个普通用户，只对AXBot的使用方式和所具备的功能感到好奇，我们为您提供了使用手册，其中详尽列出了该机器人现已具备的全部功能及正在开发中的新功能。请随时点击以下文档链接查看。

[使用手册](docs/user_guide.md)

（除了这个跟随仓库更新的文档外，我们还提供了可以在中国大陆访问的 [使用手册](https://www.yuque.com/axiangcoding/ei27mo/omy4cgwvsikrwue1)，但不一定保证为最新）

你可以通过以下方式将官方维护的AXBot机器人邀请到您的群组或者服务器中：

- Kook: [点击邀请Kook机器人](https://www.kookapp.cn/app/oauth2/authorize?id=15253&permissions=923648&client_id=eXJ0-Ntgqw-q33Oe&redirect_uri=&scope=bot)，需要邀请人的Kook账号具备邀请服务器的管理员身份
- QQ: （由于Cqhttp部署不太稳定，故不再维护qq机器人，但代码上依然cqhttp进行支持）

### 高级用户

AXBot的一个核心功能就是获取游戏数据，而这些数据会通过API接口对外提供。如果您希望能在其他的外部系统中使用这些游戏的数据，或者出于其他的目的想要对接AXBot，请联系开发者详谈。

开发者邮箱：axiangcoding@gmail.com

## 部署指南

AXBot允许自行部署，并为此提供了极其简单的操作脚本。如果您希望自己部署和维护机器人，请点击下方的文档链接，详细阅读相关内容。

[部署指南](docs/deploy_guide.md)

需要注意的是，AXBot机器人目前正在快速迭代开发中。如果您部署了AXBot机器人，我们强烈建议您更新到最新版本以获取最完整的功能和最新的错误修复。但同时，我们也建议在更新之前，详细阅读更新代码和日志并解决可能存在的兼容问题。如果您在更新过程中遇到任何问题，欢迎提出ISSUE获取帮助。

注意，自行部署机器人即代表你同意并遵循AXBot协议中规定的一切条款，这意味着你需要：

1. 自行承担服务器费用
2. 对使用中出现的问题负责
3. 对你的用户负责
4. 日常维护机器人

## 开发指南

AXBot不是一个通用机器人框架，AXBot是一个已经实现了完整业务功能的机器人系统，这意味着AXBot并未向第三方提供简单的开发方式。如果你需要开发一些新的功能，请参考下文的开发指南

[开发指南](docs/develop_guide.md)

此外需要注意的是，开发者可能会因为各种原因对代码进行结构性调整，请务必确保您是基于最新的代码进行开发的，避免出现大范围的冲突。

## 开源协议

本仓库遵循 [GPL 3.0](LICENSE) 协议。

## 赞助

机器人的开发耗时耗力，部署需要消耗服务器资源，日常的维护也需要投入资金，这意味着开发、部署和维护AXBot具有一定的成本。因此AXBot向所有用户提供免费但有限的服务是迫不得已。如果您愿意赞助AXBot或者订阅服务，您将能够享受更高的使用限制和额外的功能。更多关于付费的内容，您可以查看下面的链接

爱发电：[点击赞助](https://afdian.net/order/create?user_id=966767508b5811eca47c52540025c377&custom_price=10)
