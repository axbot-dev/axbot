# AXBot 部署指南

## 前言

尽管AXBot的服务尚未完全开发完毕，但它的部署方式相当简便。目前，我们仅支持通过docker镜像进行部署。当然，这也是我们最为推荐的部署方式。

## 部署前提

AXBot共有两个服务：core和crawler，分别作为其核心程序和数据获取节点。此外，AXBot还需要依赖第三方服务，如数据库和队列等。

默认情况下，完整部署需要依赖服务和一个crawler服务，一个core服务。这些服务需要占用约600MB的内存。因此，我们建议服务器的内存应该在2GB以上，以避免出现不必要的OOM问题。

AXBot的服务均是无状态的，您可以根据实际情况调整服务的伸缩数量，以提供稳定的服务质量

## 部署方式

### Docker Compose 部署

事实上，由于采用Docker Compose的部署方法，我们已经建立了近乎全自动的部署方式，您只需要按照以下的步骤执行部署即可。

我们假设您新购入了一台Debian服务器，您可以按照以下的方式进行部署

1. 使用apt-get更新Debian服务器
   ```bash
   sudo apt-get update -y
   sudo apt-get upgrade -y
   ```

2. 安装git
   ```bash
   apt-get install git -y
   ```

3. 下载部署仓库到您的服务器中
   ```bash
   cd $your-app-root
   git clone https://github.com/axiangcoding/AXBot-deploy.git
   ```

4. 执行安装docker脚本
   ```bash
   cd $your-app-root/AXBot-deploy
   sh ./install-docker-debian.sh
   ```

5. 配置环境变量
   在当前目录新建一个文件`.env`，并在其中填入如下配置

   ```
   DB_USER=[数据库用户名]
   DB_PASSWORD=[数据库密码，妥善保管！]
   MQ_USER=[消息队列用户名]
   MQ_PASSWORD=[消息队列密码，妥善保管！]
   
   AXBOT_TAG=[部署的镜像版本]
   
   KOOK_BOT_TOKEN=[kook机器人token]
   KOOK_VERIFY_TOKEN=[kook机器人验证token]
   ```

6. 执行服务部署脚本

   ```bash
   sh ./install-all-service.sh
   ```

7. 验证部署状态
   ```bash
   docker ps
   ```

一切顺利的话，当你执行完上述步骤后，axbot服务和所依赖的其他服务均会在你的服务器中部署完成。当然，你也可以根据您的服务器的实际情况进行调整，比如使用现有的服务替代`docker-compose.yaml`中设定的服务

### Docker 方式部署单个服务

暂不提供

### 直接部署单个服务

暂不提供，且不推荐

## 报告问题

虽然我们已经对服务的部署进行了验证，但是我们的测试环境相对较为有限。如果您在部署过程中遇到任何问题，请不要犹豫，随时提交issue并寻求帮助！我们会尽力解决您的问题。