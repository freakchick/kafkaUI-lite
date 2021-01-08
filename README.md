# kafkaUI-lite

## 介绍
- 史上最轻便好用的kafka ui界面工具，提供了对kafka的界面化操作
- 可以在界面生产消息、消费消息、创建管理topic
- 可以支持管理多个kafka集群
- 部署简便，$\color{#FF3030}{不需要连数据库，只有一个jar包启动即可}$

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里


## 安装教程

1.  依赖java环境，需要先安装jdk
2.  下载地址: https://pan.baidu.com/s/1YYVXFUtVUMFVBt9YKD49Hw 提取码: miix
2.  启动命令：java -jar kafkaUI.jar 一键启动
4.  如果是多网卡机器，启动需要指定ip

```
java -Dhost=192.168.33.201 -jar kafkaUI.jar
```
5.  浏览器访问 http://localhost:8889


## 使用说明
### kafka

#### 查看所有kafka集群
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215027.jpg)

#### 添加kafka环境
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215038.jpg)

#### 消费消息
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107214919.jpg)

#### 生产消息
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107214957.jpg)

#### 管理topic
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215009.jpg)

#### 创建topic
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107215016.jpg)

