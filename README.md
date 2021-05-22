# kafkaUI-lite

## 介绍

- 史上最轻便好用的kafka ui界面客户端工具，可以在生产消息、消费消息、管理topic、管理group;可以支持管理多个kafka集群
- 部署简便，**不需要连数据库，只有一个jar包启动即可**
- 支持zookeeper ui界面化操作;支持多环境管理
- 支持redis ui界面化操作;支持多环境管理
- 支持权限控制，可以自定义不同环境的新增、修改、删除权限；默认分配只读权限，避免用户的误操作
- 体验地址 http://47.92.117.90:8889

## 设计理念

- 轻便、简洁、易用

## 软件架构

- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库，并且数据库文件打进了jar包里

## 安装教程

- 依赖java环境，需要先安装jdk8+
- 下载地址: [天翼云盘](https://cloud.189.cn/t/f632quimaiy2), 或者在右侧发行版页面下载

**有以下3种安装方式：**

### 1.jar包安装

- 如果您想要快速安装，请下载kafka-ui-lite.jar包
- 启动命令
```shell script
java -jar kafka-ui-lite.jar
```
- 启动后浏览器访问 http://ip:8889

### 2.tar包安装

- 如果您想自定义配置，请下载kafka-ui-lite.tar.gz包
- 解压tar包，修改conf/application.properties中的以下配置，可以修改元数据库地址（mysql）和端口

```properties
server.port=8889

spring.datasource.driver-class-name=org.sqlite.JDBC
spring.datasource.url=jdbc:sqlite::resource:data.db 
spring.datasource.username=
spring.datasource.password=
```
*如果您修改了元数据库为您自己的mysql，请在mysql执行数据库初始化sql脚本，脚本在sql目录下*

- linux操作命令

```shell
# 前台启动
sh bin/kafkaUI.sh start
# 后台启动
sh bin/kafkaUI.sh -d start
# 关闭后台启动的进程
sh bin/kafkaUI.sh stop

```

- windows操作命令
```shell
# 前台启动
bin/kafkaUI.bat
```
或者直接双击 bin/kafkaUI.bat 文件启动

- 启动后浏览器访问 http://ip:8889

### 3.docker安装

```shell script
  docker run -d -p 8889:8889 freakchicken/kafka-ui-lite
```

- 启动后浏览器访问 http://ip:8889

## 使用说明

### kafka操作

#### 查看所有kafka集群
![](https://freakchicken.gitee.io/images/kafkaui/20210522/kafka_config.png)

#### 添加kafka环境
![](https://freakchicken.gitee.io/images/kafkaui/20210522/kafka_config.png)

#### 消费消息
![](https://freakchicken.gitee.io/images/kafkaui/20210522/consume.png)

#### 生产消息
![](https://freakchicken.gitee.io/images/kafkaui/20210522/produce.png)

#### 管理topic
![](https://freakchicken.gitee.io/images/kafkaui/20210522/topic_manage.png)

#### 查看topic详情
![](https://freakchicken.gitee.io/images/kafkaui/20210522/topic_detail.png)

#### 查看topic被消费的所有group
![](https://freakchicken.gitee.io/images/kafkaui/20210522/view_group.png)

#### 管理集群
![](https://freakchicken.gitee.io/images/kafkaui/20210522/cluster_manage.png)

#### 管理group
![](https://freakchicken.gitee.io/images/kafkaui/20210522/group_manage.png)

#### 查看group消费偏移量详情
![](https://freakchicken.gitee.io/images/kafkaui/20210522/group_detail.png)

#### 创建topic
![](https://freakchicken.gitee.io/images/kafkaui/20210522/add_topic.png)

### zookeeper操作
#### 查看所有zk集群
![](https://freakchicken.gitee.io/images/kafkaui/20210522/zk_config.png)
#### 添加zk集群
![](https://freakchicken.gitee.io/images/kafkaui/20210522/zk_env_add.png)

#### 查看节点数据
![](https://freakchicken.gitee.io/images/kafkaui/20210522/zk_manage.png)
#### 添加节点
![](https://freakchicken.gitee.io/images/kafkaui/20210522/zk_add_node.png)

### redis操作

#### 添加redis环境

![](https://freakchicken.gitee.io/images/kafkaui/redis_add_source_20210112143453.jpg)

#### 查看所有redis环境

![](https://freakchicken.gitee.io/images/kafkaui/redis_sources_20210112143546.jpg)

#### 添加redis key

![](https://freakchicken.gitee.io/images/kafkaui/redis_add_hash_20210112143738.jpg)
![](https://freakchicken.gitee.io/images/kafkaui/redis_add_string_20210112143815.jpg)
![](https://freakchicken.gitee.io/images/kafkaui/redis_add_list_20210112143759.jpg)

#### 查看数据

![](https://freakchicken.gitee.io/images/kafkaui/redis_string_detail_20210112143708.jpg)
![](https://freakchicken.gitee.io/images/kafkaui/redis_set_detail_20210112143642.jpg)
![](https://freakchicken.gitee.io/images/kafkaui/redis_hash_detail_20210112143618.jpg)

#### 删除redis key

![](https://freakchicken.gitee.io/images/kafkaui/redis_delete_key_20210112143842.jpg)

### 权限控制

![](https://freakchicken.gitee.io/images/kafkaui/20210201/auth.jpg)

## 开发指南

### 环境依赖

- 安装jdk8+
- 安装node.js
- 安装cnpm (maven 会调用cnpm 系统命令)

```shell
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

### 编译打包

- maven打包会自动把前端安装依赖并编译打包，

```shell
mvn clean package -P tar
mvn clean package -P singleJar
```
### 构建镜像
```shell
mvn docker:build -P tar
mvn docker:push -P tar
```
### 启动

#### 前端启动：

- src/main/webapp 目录下 **npm run serve**

#### 后端启动

- 启动主类com.jq.kafkaui.KafkaUIApplication

### 前端访问地址：

```
http://localhost:8181
```

### 后端接口访问地址：

```
http://localhost:8889
```

## 联系作者：

### 微信：
- 提问请先star支持一下
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>

### qq交流群：
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/qqgroup.jpg" width = "40%" />
</div>

### 捐赠：

如果您喜欢这个项目，请作者喝杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
<img src="https://freakchicken.gitee.io/images/kafkaui/alipay.jpg" width = "29%" />
</div>

## 友情推荐

### DBAPI

- 零代码开发后端接口，只需要编写sql就能生成http Api，可以动态添加api
  [gitee](https://gitee.com/freakchicken/db-api)
  [github](https://github.com/freakchick/DBApi)
