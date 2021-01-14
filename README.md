# kafkaUI-lite

## 介绍
- 史上最轻便好用的kafka ui界面客户端工具，提供了对kafka的界面化操作
- 可以在界面生产消息、消费消息、管理topic、管理group;可以支持管理多个kafka集群
- 部署简便，**不需要连数据库，只有一个jar包启动即可**
- 支持zookeeper ui界面化操作;支持多环境管理
- 支持redis ui界面化操作;支持多环境管理

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库，并且数据库文件打进了jar包里


## 安装教程

1.  依赖java环境，需要先安装jdk
2.  下载地址: https://gitee.com/freakchicken/kafka-ui-lite/releases
2.  启动命令：java -jar kafkaUI.jar 一键启动
4.  如果是多网卡机器，启动需要指定ip

```
java -Dhost=192.168.33.201 -jar kafkaUI.jar
```
5.  浏览器访问 http://localhost:8889


## 使用说明
### kafka操作

#### 查看所有kafka集群
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215027.jpg)

#### 添加kafka环境
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215038.jpg)

#### 消费消息
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107214919.jpg)

#### 生产消息
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107214957.jpg)

#### 管理topic
![](https://freakchicken.gitee.io/kafka-ui-lite/kafka_topic_manage_20210110184715.jpg)

#### 管理集群
![](https://freakchicken.gitee.io/kafka-ui-lite/kafka_cluster_20210112144820.jpg)

#### 管理group
![](https://freakchicken.gitee.io/kafka-ui-lite/group_manage_20210110184621.jpg)

#### 查看group消费偏移量详情
![](https://freakchicken.gitee.io/kafka-ui-lite/group_detail_20210110184557.jpg)

#### 创建topic
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215016.jpg)

#### 查看topic分区详情
![](https://freakchicken.gitee.io/kafka-ui-lite/topic_detail_20210112144638.jpg)


### zookeeper操作
#### 添加zk集群
![](https://freakchicken.gitee.io/kafka-ui-lite/zk_add_source_20210110121408.png)

#### 查看所有zk集群
![](https://freakchicken.gitee.io/kafka-ui-lite/zk_source_20210110121439.png)

#### 添加节点
![](https://freakchicken.gitee.io/kafka-ui-lite/zk_add_node_20210110121530.png)

#### 查看节点数据
![](https://freakchicken.gitee.io/kafka-ui-lite/zkmanage_20210110121509.png)

### redis操作
#### 添加redis环境
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_add_source_20210112143453.jpg)

#### 查看所有redis环境
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_sources_20210112143546.jpg)

#### 添加redis key
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_add_hash_20210112143738.jpg)
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_add_string_20210112143815.jpg)
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_add_list_20210112143759.jpg)

#### 查看数据
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_string_detail_20210112143708.jpg)
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_set_detail_20210112143642.jpg)
![](https://freakchicken.gitee.io/kafka-ui-lite/redis_hash_detail_20210112143618.jpg)

#### 删除redis key

![](https://freakchicken.gitee.io/kafka-ui-lite/redis_delete_key_20210112143842.jpg)

## 联系作者：
### wechat：
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/kafka-ui-lite/wechat.jpg" width = "30%" />
</div>


### 捐赠：
如果您喜欢此项目，请给作者捐助5块钱去买生姜洗发水，来保留为数不多的头发
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/kafka-ui-lite/wechatpay.jpg" width = "30%" />
</div>