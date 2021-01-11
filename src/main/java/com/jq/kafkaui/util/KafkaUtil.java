package com.jq.kafkaui.util;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.ConsumerGroupState;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-28 20:05
 **/
@Slf4j
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

    public static List<Topic> listTopicsWithOptions(String brokers) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);

        ListTopicsOptions options = new ListTopicsOptions();
        // 列出内部的Topic
        options.listInternal(true);

        // 列出所有的topic
        ListTopicsResult result = adminClient.listTopics(options);
        Collection<TopicListing> topicListings = result.listings().get();

        List<Topic> collect = topicListings.stream().map(t -> {
            Topic topic = new Topic();
            topic.setName(t.name());
            topic.setInternal(t.isInternal());
            return topic;
        }).sorted(Comparator.comparing(t -> t.getName())).collect(Collectors.toList());

        adminClient.close();
        return collect;

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

    public static void createTopic(String brokers, String topic, Integer partition, Integer replica) throws Exception {
        // 创建AdminClient客户端对象
        AdminClient adminClient = createAdminClientByProperties(brokers);
        List<NewTopic> topicList = new ArrayList();
        NewTopic newTopic = new NewTopic(topic, partition, replica.shortValue());
        topicList.add(newTopic);
        CreateTopicsResult result = adminClient.createTopics(topicList);
        // get方法是一个阻塞方法，一定要等到createTopics完成之后才进行下一步操作
        result.all().get();
        // 打印新创建的topic名
        result.values().forEach((name, future) -> System.out.println("topicName:" + name));
        // 关闭资源
        adminClient.close();
    }

    public static Producer<String, String> getProducer(String brokers) {

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        return producer;

    }

    public static KafkaConsumer<String, String> getConsumer(String brokers, String topic, String group, String offset) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", brokers);
        props.setProperty("group.id", group);
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("auto.offset.reset", offset);
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;

    }

    public static void main(String[] args) throws Exception {

    }

    public static void deleteTopic(String broker, String name) {
        AdminClient adminClient = createAdminClientByProperties(broker);
        List<String> list = new ArrayList<>();
        list.add(name);
        adminClient.deleteTopics(list);
        adminClient.close();
    }

    public static JSONObject getTopicDetail(String broker, String topic) throws Exception {
        AdminClient adminClient = createAdminClientByProperties(broker);
        List<String> list = new ArrayList<>();
        list.add(topic);
        DescribeTopicsResult result = adminClient.describeTopics(list);
        Map<String, TopicDescription> map = result.all().get();
        TopicDescription topicDescription = map.get(topic);

        JSONObject res = new JSONObject();
        res.put("isInternal", topicDescription.isInternal());
        res.put("name", topicDescription.name());
        List<JSONObject> collect = topicDescription.partitions().stream().map(t -> {
            JSONObject p = new JSONObject();
            Node leader = t.leader();
            log.info(leader.toString());

            List<String> replicas = t.replicas().stream().map(r -> {
                return r.toString();
            }).collect(Collectors.toList());

            List<String> isr = t.isr().stream().map(r -> {
                return r.toString();
            }).collect(Collectors.toList());
            ;

            p.put("partition", t.partition());
            p.put("leader", t.leader().toString());
            p.put("replicas", replicas);
            p.put("isr", isr);

            return p;

        }).collect(Collectors.toList());
        res.put("partitions", collect);

        System.out.println(res.toJSONString());
        adminClient.close();
        return res;
    }

    public static List<JSONObject> clusterInfo(String broker) throws Exception {
        AdminClient client = createAdminClientByProperties(broker);
        DescribeClusterResult describeClusterResult = client.describeCluster();

        Collection<Node> nodes = describeClusterResult.nodes().get();
        List<JSONObject> collect = nodes.stream().map(node -> {
            JSONObject jo = new JSONObject();
            jo.put("host", node.host());
            jo.put("port", node.port());
            jo.put("idStr", node.idString());
            jo.put("id", node.id());
            return jo;
        }).collect(Collectors.toList());
        return collect;

    }

    public static List<JSONObject> getAllGroups(String broker) throws Exception {
        AdminClient client = createAdminClientByProperties(broker);
        ListConsumerGroupsResult listConsumerGroupsResult = client.listConsumerGroups();
        Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all().get();
        List<JSONObject> collect = consumerGroupListings.stream().map(t -> {

            JSONObject jo = new JSONObject();
            jo.put("name", t.groupId());
            return jo;
        }).collect(Collectors.toList());
        return collect;

    }

    public static Collection<List<JSONObject>> getGroupInfo(String broker, String group) throws Exception {
        AdminClient client = createAdminClientByProperties(broker);
        ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = client.listConsumerGroupOffsets(group);

        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get();
        Collection<OffsetAndMetadata> values = topicPartitionOffsetAndMetadataMap.values();

        Set<TopicPartition> topicPartitions = topicPartitionOffsetAndMetadataMap.keySet();

        Map<String, List<JSONObject>> collect = topicPartitions.stream().map(t -> {
            OffsetAndMetadata offsetAndMetadata = topicPartitionOffsetAndMetadataMap.get(t);
            long offset = offsetAndMetadata.offset();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("topic", t.topic());
            jsonObject.put("partition", t.partition());
            jsonObject.put("offset", offset);
            return jsonObject;
        }).collect(Collectors.groupingBy(t -> {
            return t.getString("topic");
        }));
        Collection<List<JSONObject>> values1 = collect.values();
        client.close();
        return values1;

    }

    public static void deleteGroup(String broker, String group) throws Exception {
        AdminClient client = createAdminClientByProperties(broker);
        List<String> list = new ArrayList<>();
        list.add(group);
        DeleteConsumerGroupsResult deleteConsumerGroupsResult = client.deleteConsumerGroups(list);
        Void aVoid = deleteConsumerGroupsResult.all().get();
        client.close();

    }

    public static Collection<Node> group(String broker) throws Exception {
        AdminClient client = createAdminClientByProperties(broker);
        ListConsumerGroupsResult result = client.listConsumerGroups();

        Collection<ConsumerGroupListing> consumerGroupListings = result.all().get();
        consumerGroupListings.stream().forEach(t -> {
            String groupId = t.groupId();
            ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = client.listConsumerGroupOffsets(groupId);
            try {
                Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        });

        return null;

    }
}
