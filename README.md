# kafkaUI-lite

## 介绍
- 史上最轻便好用的kafka ui界面客户端工具，可以在生产消息、消费消息、管理topic、管理group;可以支持管理多个kafka集群
- 部署简便，**不需要连数据库，只有一个jar包启动即可**
- 支持zookeeper ui界面化操作;支持多环境管理
- 支持redis ui界面化操作;支持多环境管理
- 支持权限控制，可以自定义不同环境的新增、修改、删除权限；默认分配只读权限，避免用户的误操作

## 设计理念
- 轻便、简洁、易用

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库，并且数据库文件打进了jar包里


## 安装教程

-  依赖java环境，需要先安装jdk8+
-  下载地址: [gitee](https://gitee.com/freakchicken/kafka-ui-lite/releases) [github](https://github.com/freakchick/DBApi/releases)

-  启动命令：java -jar kafkaUI.jar 一键启动
-  如果是多网卡机器，启动需要指定ip
```shell script
java -Dhost=xx.xx.xx.xx -jar kafkaUI.jar
```
-  浏览器访问 http://ip:8889


## 使用说明
### kafka操作

#### 查看所有kafka集群
![](https://freakchicken.gitee.io/images/kafkaui/20210201/kafka_addSource.jpg)

#### 添加kafka环境
![](https://freakchicken.gitee.io/images/kafkaui/Dingtalk_20210107215038.jpg)

#### 消费消息
![](https://freakchicken.gitee.io/images/kafkaui/20210329/consume.jpg)

#### 生产消息
![](https://freakchicken.gitee.io/images/kafkaui/20210201/produce.jpg)

#### 管理topic
![](https://freakchicken.gitee.io/images/kafkaui/20210201/topic_list.jpg)

#### 查看所有topic详情
![](https://freakchicken.gitee.io/images/kafkaui/20210201/topic_detail.jpg)

#### 管理集群
![](https://freakchicken.gitee.io/images/kafkaui/20210201/cluster_manage.jpg)

#### 管理group
![](https://freakchicken.gitee.io/images/kafkaui/20210201/group_list.jpg)

#### 查看group消费偏移量详情
![](https://freakchicken.gitee.io/images/kafkaui/20210201/group_detail.jpg)

#### 创建topic
![](https://freakchicken.gitee.io/images/kafkaui/Dingtalk_20210107215016.jpg)


### zookeeper操作
#### 添加zk集群
![](https://freakchicken.gitee.io/images/kafkaui/zk_add_source_20210110121408.png)

#### 查看所有zk集群
![](https://freakchicken.gitee.io/images/kafkaui/zk_source_20210110121439.png)

#### 添加节点
![](https://freakchicken.gitee.io/images/kafkaui/zk_add_node_20210110121530.png)

#### 查看节点数据
![](https://freakchicken.gitee.io/images/kafkaui/zkmanage_20210110121509.png)

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

```
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

### 编译打包

- maven打包会自动把前端安装依赖并编译打包，

```
mvn clean package
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
### wechat：
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>


### 捐赠：
如果您喜欢这个项目，请作者喝杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
<img src="https://freakchicken.gitee.io/images/kafkaui/alipay.jpg" width = "33%" />
</div>

## 友情推荐
### DBAPI
- 零代码开发后端接口，只需要编写sql就能生成http Api，可以动态添加api
[gitee](https://gitee.com/freakchicken/db-api)
[github](https://github.com/freakchick/DBApi)
