package com.jq.kafkaui.util;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.ConfigResource;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-28 20:05
 **/
public class KafkaUtil {

    public static AdminClient createAdminClientByProperties(String brokers) {

        Properties prop = new Properties();

        // 配置Kafka服务的访问地址及端口号
        prop.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        prop.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
        prop.setProperty(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "2000");

        // 创建AdminClient实例
        return AdminClient.create(prop);
    }

    public static List<String> listTopicsWithOptions(String brokers) {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);

        ListTopicsOptions options = new ListTopicsOptions();
        // 列出内部的Topic
        options.listInternal(true);

        // 列出所有的topic
        ListTopicsResult result = adminClient.listTopics(options);

        try {
            Set<String> topicNames = result.names().get();
            return new ArrayList<>(topicNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

            adminClient.close();
        }

    }

    /**
     * 获取topic的描述信息
     * <p>
     * topic name = a-topic, desc = (name=a-topic, internal=false, partitions=(partition=0, leader=192.168.182.128:9092 (id: 0 rack: null), replicas=192.168.182.128:9092 (id: 0 rack: null), isr=192.168.182.128:9092 (id: 0 rack: null)), authorizedOperations=null)
     * topic name = b-topic, desc = (name=b-topic, internal=false, partitions=(partition=0, leader=192.168.182.128:9092 (id: 0 rack: null), replicas=192.168.182.128:9092 (id: 0 rack: null), isr=192.168.182.128:9092 (id: 0 rack: null)), authorizedOperations=null)
     */
    public static void describeTopics(List<String> topics, String brokers) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);

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
    public static void describeConfigTopics(List<String> topicNames, String brokers) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);

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
    public static void createTopic(List<String> topicNames, String brokers) throws Exception {
// 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);

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

    public static void producer(String brokers, String topic) {

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<>(topic, "ddd"));

        producer.close();

    }

    public static void consumer(String brokers, String group, String topic) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", brokers);
        props.setProperty("group.id", group);
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("jiang");
        list.add("1iang");
//        createTopic(list);
//        listTopicsWithOptions();

//        describeTopics(list);
    }

}
