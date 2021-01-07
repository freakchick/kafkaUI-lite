# kafkaUI-lite

## 介绍
- 史上最轻便好用的kafka ui界面工具，提供了对kafka的界面化操作
- 可以在界面生产消息、消费消息、创建topic。同时可以支持管理多个kafka集群
- 部署简便，不需要连数据库，只有一个jar包启动即可

## 软件架构
- springboot + vue.js 前后端分离开发，考虑到部署的简便性，最后打包是整合打到一个完整的jar包里


## 安装教程

1.  依赖java环境，需要先安装jdk
2.  下载地址: https://pan.baidu.com/s/1YYVXFUtVUMFVBt9YKD49Hw 提取码: miix
2.  启动命令：java -jar kafkaUI.jar 一键启动
4.  多网卡机器启动需要指定ip
```
java -Dhost=192.168.33.201 -jar kafkaUI.jar
```

## 使用说明
### kafka
#### 添加kafka环境
![](https://freakchicken.gitee.io/kafka-ui-lite/config.jpg)

#### 消费消息
![](https://freakchicken.gitee.io/kafka-ui-lite/Dingtalk_20210107214919.jpg)

#### 创建topic
![](https://freakchicken.gitee.io/kafka-ui-lite/config.jpg)

#### 发送消息
![](https://freakchicken.gitee.io/kafka-ui-lite/config.jpg)

#### 查看所有topic
![](https://freakchicken.gitee.io/kafka-ui-lite/config.jpg)

#### 查看所有kafka集群
![](hhttps://freakchicken.gitee.io/kafka-ui-lite/config.jpg)
