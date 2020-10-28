package com.jq.kafkaui.util;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-28 20:05
 **/
public class KafkaUtil {

    public static AdminClient createAdminClientByProperties() {

        Properties prop = new Properties();

        // 配置Kafka服务的访问地址及端口号
        prop.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.33.201:9092");

        // 创建AdminClient实例
        return AdminClient.create(prop);
    }

    public static void listTopicsWithOptions() throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties();

        ListTopicsOptions options = new ListTopicsOptions();
        // 列出内部的Topic
        options.listInternal(true);

        // 列出所有的topic
        ListTopicsResult result = adminClient.listTopics(options);

        // 获取所有topic的名字，返回的是一个Set集合
        Set<String> topicNames = result.names().get();

        topicNames.forEach(System.out::println);

        // 获取所有topic的信息，返回的是一个Collection集合
        // (name=hello-kafka, internal=false),internal代表是否为内部的topic
        Collection<TopicListing> topicListings = result.listings().get();

        // 打印所有topic的信息
        topicListings.forEach(System.out::println);

        // 关闭资源
        adminClient.close();
    }

    /**
     * 获取topic的描述信息
     * <p>
     * topic name = a-topic, desc = (name=a-topic, internal=false, partitions=(partition=0, leader=192.168.182.128:9092 (id: 0 rack: null), replicas=192.168.182.128:9092 (id: 0 rack: null), isr=192.168.182.128:9092 (id: 0 rack: null)), authorizedOperations=null)
     * topic name = b-topic, desc = (name=b-topic, internal=false, partitions=(partition=0, leader=192.168.182.128:9092 (id: 0 rack: null), replicas=192.168.182.128:9092 (id: 0 rack: null), isr=192.168.182.128:9092 (id: 0 rack: null)), authorizedOperations=null)
     */
    public static void describeTopics(List<String> topics) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties();

        // 获取Topic的描述信息
        DescribeTopicsResult result = adminClient.describeTopics(topics);

        // 解析描述信息结果, Map<String, TopicDescription> ==> topicName:topicDescription
        Map<String, TopicDescription> topicDescriptionMap = result.all().get();
        topicDescriptionMap.forEach((topicName, description) -> System.out.printf("topic name = %s, desc = %s \n", topicName, description));

        // 关闭资源
        adminClient.close();
    }

    /**
     * 获取topic的配置信息
     */
    public static void describeConfigTopics(List<String> topicNames) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties();

        List<ConfigResource> configResources = new ArrayList<>();
        topicNames.forEach(topicName -> configResources.add(
                // 指定要获取的源
                new ConfigResource(ConfigResource.Type.TOPIC, topicName)));

        // 获取topic的配置信息
        DescribeConfigsResult result = adminClient.describeConfigs(configResources);

        // 解析topic的配置信息
        Map<ConfigResource, Config> resourceConfigMap = result.all().get();
        resourceConfigMap.forEach((configResource, config) -> System.out.printf("topic config ConfigResource = %s, Config = %s \n", configResource, config));

        // 关闭资源
        adminClient.close();
    }

    /**
     * 创建一个或多个topic
     *
     * @param topicNames topic名称的集合
     */
    public static void createTopic(List<String> topicNames) throws Exception {
// 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties();

        List<NewTopic> topicList = new ArrayList();
/**
 * 定义topic信息
 * String name                topic名
 * int numPartitions          分区数
 * short replicationFactor    副本数,必须不能大于broker数量
 */
        topicNames.forEach(topicName -> topicList.add(
                new NewTopic(topicName, 1, Short.parseShort("1"))));

// 创建topic
        CreateTopicsResult result = adminClient.createTopics(topicList);

// get方法是一个阻塞方法，一定要等到createTopics完成之后才进行下一步操作
        result.all().get();

// 打印新创建的topic名
        result.values().forEach((name, future) -> System.out.println("topicName:" + name));

// 关闭资源
        adminClient.close();
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("jiang");
        list.add("1iang");
//        createTopic(list);
//        listTopicsWithOptions();

        describeTopics(list);
    }

}
