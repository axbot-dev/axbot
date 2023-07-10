<p align="center">
 <img src="docs/axbot_logo.png" height="200px" />   
</p>

# AXBot 艾克斯机器人

[![GitHub](https://img.shields.io/github/license/axiangcoding/AXBot)](LICENSE) [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/axiangcoding/axbot-core?label=axbot-core&sort=semver)](https://hub.docker.com/r/axiangcoding/axbot-core) [![Docker Image Version (latest semver)](https://img.shields.io/docker/v/axiangcoding/axbot-core?label=axbot-crawler&sort=semver)](https://hub.docker.com/r/axiangcoding/axbot-crawler) [![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/axiangcoding/AXBot/build_docker_image.yml?label=build_docker_image)](https://github.com/axiangcoding/AXBot/actions/workflows/build_docker_image.yml) [![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/axiangcoding/AXBot/codeql.yml?label=codeql)](https://github.com/axiangcoding/AXBot/actions/workflows/codeql.yml) [![Codacy grade](https://img.shields.io/codacy/grade/501ee223d049451d9de502036fab1ce1)](https://app.codacy.com/gh/axiangcoding/AXBot/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

> **开发者正在开发v2的功能，v1和v2版本将是类似但不兼容的版本。项目不再对v1版本进行支持**

## 简介

AXBot，全称艾克斯机器人（以下简称 "AXBot"），是一个在Kook、QQ频道等社交平台上可用的机器人。她不仅具备丰富的交互功能，还专注于为游戏爱好者提供各种游戏相关的服务。作为一款功能强大的游戏类问答机器人，AXBot的终极目标是协助玩家获取游戏数据、分析游戏数据，智能回答游戏攻略，并实现社交平台与游戏的无缝连接。

AXBot目前具备以下功能和能力：

- 可以在KOOK和QQ频道上运行
- 能够查询《战争雷霆》的战绩信息
- 支持查询和记录《战争雷霆》玩家的信誉度
- 可以播报《战争雷霆》官网的最新新闻
- 当B站直播间开播时提醒特定人群

同时，AXBot还具备以下一些有趣的小功能：

- 每日电子算命，获取当天的气运值
- 和ChatGPT聊天

AXBot的设计思路源自于https://github.com/axiangcoding/antonstar-bot ，这是开发者在聊天机器人领域的首次尝试。由于各种原因，asbot已经完成了其生命周期，而AXBot则是对asbot的延续和扩展。目前，AXBot已经完全迁移并运行。

走过路过，点个`⭐`吧

> 艾克斯机器人的中文名是基于音译而来的，而 "AX" 则是作者平时使用的昵称，并没有特殊的含义。

## 使用手册
### 普通用户

如果您对AXBot的使用方式和功能感到好奇，我们为您提供了详尽的使用手册，其中详细列出了机器人目前已具备的所有功能以及正在开发中的新功能。您可以随时点击以下链接查看使用手册：

[使用手册](https://www.yuque.com/axiangcoding/ei27mo/omy4cgwvsikrwue1)

您可以通过以下方式将官方维护的AXBot机器人邀请到您的群组或服务器中：

- Kook：[点击此处邀请Kook机器人](https://www.kookapp.cn/app/oauth2/authorize?id=15253&permissions=923648&client_id=eXJ0-Ntgqw-q33Oe&redirect_uri=&scope=bot)，请确保邀请人的Kook账号具备邀请服务器的管理员身份。
- QQ频道：暂无

### 进阶用户

如果您希望在其他外部系统中使用AXBot提供的游戏数据（比如战争雷霆的玩家战绩数据等），或者有其他想要与AXBot进行对接的需求，请与开发者联系进行详细讨论。开发者将为您提供相关接口，并协助您实现所需的功能。

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

AXBot不是一个通用的机器人框架，而是一个完整的机器人系统，已经实现了所有业务功能。这意味着AXBot没有提供简单的第三方开发方式。如果您想要开发新功能，您需要在AXBot的代码基础上进行开发。请参考下文的开发指南，以便在您的开发环境中部署AXBot：

[开发指南](docs/develop_guide.md)

此外，请注意，开发者可能会对代码进行结构性调整，出于各种原因。因此，请确保您基于最新的代码进行开发，以避免出现大范围的冲突。

## 开源协议

本仓库遵循 [GPL 3.0](LICENSE) 协议。

## 赞助

机器人的开发过程耗费了大量时间和精力，而其部署需要消耗大量服务器资源，此外，日常的维护也需要不断投入资金。这意味着开发、部署和维护AXBot都具有一定的成本。因此，为了提供服务，AXBot不得不向所有用户提供免费但有限的功能。然而，如果您愿意赞助AXBot或订阅我们的服务，您将能够享受更高的使用限制和额外的功能。关于付费方面的更多信息，请查看以下链接：

爱发电：[点击赞助](https://afdian.net/order/create?user_id=966767508b5811eca47c52540025c377&custom_price=10)
