package com.jq.kafkaui.util;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.Topic;
import com.jq.kafkaui.dto.ResponseDto;
import com.jq.kafkaui.dto.SourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;

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

    public static AdminClient createAdminClientByProperties(SourceInfo sourceInfo) {

        Properties prop = getCommonProperties(sourceInfo);

        prop.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, sourceInfo.getBroker());
        prop.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
        prop.setProperty(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "2000");
        return AdminClient.create(prop);
    }

    private static Properties getCommonProperties(SourceInfo sourceInfo) {
        Properties prop = new Properties();
        String userName = sourceInfo.getUserName();
        String password = sourceInfo.getPassword();

        prop.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username="
                + userName + " password=" + password + ";");
        prop.put("security.protocol", "SASL_PLAINTEXT");
        prop.put("sasl.mechanism", "PLAIN");
        return prop;
    }


    public static ResponseDto listTopicsWithOptions(SourceInfo sourceInfo, String keyword) {
        AdminClient adminClient = null;
        try {
            // 创建AdminClient客户端对象
            adminClient = createAdminClientByProperties(sourceInfo);

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

            if (keyword != null) {
                collect = collect.stream().filter(t -> t.getName().contains(keyword)).collect(Collectors.toList());
            }

            ResponseDto success = ResponseDto.success(collect);
            return success;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseDto.fail(e.getMessage());
        } finally {

            adminClient.close();
        }

    }

    public static void createTopic(SourceInfo sourceInfo, String topic, Integer partition, Integer replica) throws Exception {
        AdminClient adminClient = null;
        try {
            adminClient = createAdminClientByProperties(sourceInfo);
            List<NewTopic> topicList = new ArrayList();
            NewTopic newTopic = new NewTopic(topic, partition, replica.shortValue());
            topicList.add(newTopic);
            CreateTopicsResult result = adminClient.createTopics(topicList);
            result.all().get();
            result.values().forEach((name, future) -> System.out.println("topicName:" + name));
        } catch (Exception e) {

        } finally {

            adminClient.close();
        }

    }

    public static Producer<String, String> getProducer(SourceInfo sourceInfo) {

        Properties props = getCommonProperties(sourceInfo);
        props.put("bootstrap.servers", sourceInfo.getBroker());
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);

        return producer;

    }

    public static KafkaConsumer<String, String> getConsumer(SourceInfo sourceInfo, String topic, String group, String offset) {
        Properties props = getCommonProperties(sourceInfo);
        props.setProperty("bootstrap.servers", sourceInfo.getBroker());
        props.setProperty("group.id", group);
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("auto.offset.reset", offset);
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singleton(topic));
        return consumer;

    }

    public static KafkaConsumer<String, String> getConsumer(SourceInfo sourceInfo, Collection<String> topics, String group, String offset) {
        Properties props = getCommonProperties(sourceInfo);
        props.setProperty("bootstrap.servers", sourceInfo.getBroker());
        props.setProperty("group.id", group);
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("auto.offset.reset", offset);
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(topics);
        return consumer;

    }

    public static void main(String[] args) throws Exception {

        /*KafkaConsumer<String, String> consumer = getConsumer("47.92.117.90:9092", "aaa", "test", "earliest");
        List<PartitionInfo> partitionInfoSet = consumer.partitionsFor("aaa");

        List<TopicPartition> collect = partitionInfoSet.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
                .collect(Collectors.toList());
//        consumer.assign(collect);
        Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(collect);
        Map<TopicPartition, Long> topicPartitionLongMap1 = consumer.beginningOffsets(collect);

//        consumer.poll(Duration.ofMillis(0));
//        consumer.assign(collect);
//        List<Long> positions = collect.stream().map(t -> {
//            long position = consumer.position(t);
//            return position;
//        }).collect(Collectors.toList());

        System.out.println();*/
    }

    public static void deleteTopic(SourceInfo sourceInfo, String name) {
        AdminClient adminClient = createAdminClientByProperties(sourceInfo);
        List<String> list = new ArrayList<>();
        list.add(name);
        adminClient.deleteTopics(list);
        adminClient.close();
    }

    public static JSONObject node2Json(Node node) {
        JSONObject leaderNode = new JSONObject();
        leaderNode.put("id", node.id());
        leaderNode.put("host", node.host());
        leaderNode.put("port", node.port());
        leaderNode.put("rack", node.rack());
//        leaderNode.put("port",node.port());
        return leaderNode;
    }

    public static JSONObject getTopicDetail(SourceInfo sourceInfo, String topic) throws Exception {
        AdminClient adminClient = createAdminClientByProperties(sourceInfo);

        List<String> list = new ArrayList<>();
        list.add(topic);
        DescribeTopicsResult result = adminClient.describeTopics(list);
        Map<String, TopicDescription> map = result.all().get();
        TopicDescription topicDescription = map.get(topic);

        JSONObject res = new JSONObject();
        res.put("isInternal", topicDescription.isInternal());
        res.put("name", topicDescription.name());

        KafkaConsumer<String, String> consumer = getConsumer(sourceInfo, topic, "KafkaUI-lite", "earliest");
        List<TopicPartition> topicPartitions = topicDescription.partitions().stream().map(t -> {
            TopicPartition topicPartition = new TopicPartition(topic, t.partition());
            return topicPartition;
        }).collect(Collectors.toList());
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
        Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(topicPartitions);

        List<JSONObject> collect = topicDescription.partitions().stream().map(t -> {
            JSONObject p = new JSONObject();
            Node leader = t.leader();
            log.info(leader.toString());

            List<JSONObject> replicas = t.replicas().stream().map(r -> node2Json(r)).collect(Collectors.toList());

            List<JSONObject> isr = t.isr().stream().map(r -> node2Json(r)).collect(Collectors.toList());
            Long endOffset = endOffsets.get(new TopicPartition(topic, t.partition()));
            Long beginningOffset = beginningOffsets.get(new TopicPartition(topic, t.partition()));

            p.put("partition", t.partition());

//            leaderNode.put("id",leader.id());
            p.put("leader", node2Json(leader));
            p.put("replicas", replicas);
            p.put("isr", isr);
            p.put("endOffset", endOffset);
            p.put("beginningOffset", beginningOffset);

            return p;

        }).collect(Collectors.toList());
        res.put("partitions", collect);

        System.out.println(res.toJSONString());
        adminClient.close();
        return res;
    }

    public static ResponseDto clusterInfo(SourceInfo sourceInfo) {
        AdminClient client = null;
        try {
            client = createAdminClientByProperties(sourceInfo);
            DescribeClusterResult describeClusterResult = client.describeCluster();
            Node controller = describeClusterResult.controller().get();
            Collection<Node> nodes = describeClusterResult.nodes().get();
            List<JSONObject> collect = nodes.stream().map(node -> {
                JSONObject jo = new JSONObject();
                jo.put("host", node.host());
                jo.put("port", node.port());
                jo.put("idStr", node.idString());
                jo.put("id", node.id());
                if (node.id() == controller.id()) {
                    jo.put("controller", true);
                } else {
                    jo.put("controller", false);
                }
                return jo;
            }).collect(Collectors.toList());
            return ResponseDto.success(collect);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            client.close();
        }

    }

    public static ResponseDto getAllGroups(SourceInfo sourceInfo, String keyword) {
        AdminClient client = null;
        try {
            client = createAdminClientByProperties(sourceInfo);
            ListConsumerGroupsResult listConsumerGroupsResult = client.listConsumerGroups();
            Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all().get();
            List<JSONObject> collect = consumerGroupListings.stream().map(t -> {
                JSONObject jo = new JSONObject();
                jo.put("name", t.groupId());
                return jo;
            }).collect(Collectors.toList());
            if (keyword != null) {
                collect = collect.stream().filter(t -> t.getString("name").contains(keyword)).collect(Collectors.toList());
            }

            return ResponseDto.success(collect);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            client.close();
        }

    }

    public static ResponseDto getGroupByTopic(SourceInfo sourceInfo, String topic) {
        AdminClient client = null;
        try {
            client = createAdminClientByProperties(sourceInfo);

            AdminClient finalClient = client;

            List<JSONObject> collect = client.listConsumerGroups().all().get().parallelStream().map(t -> t.groupId()).filter(group -> {
                long count = 0;
                try {
                    count = finalClient.listConsumerGroupOffsets(group).partitionsToOffsetAndMetadata().get().keySet().stream().filter(p -> {
                        return p.topic().equals(topic);
                    }).count();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return count > 0;
            }).map(t -> {
                JSONObject object = new JSONObject();
                object.put("value", t);
                return object;
            }).collect(Collectors.toList());
            return ResponseDto.success(collect);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            client.close();
        }

    }

    public static ResponseDto getGroupInfo(SourceInfo sourceInfo, String group) {
        AdminClient client = null;
        try {

            client = createAdminClientByProperties(sourceInfo);
            ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = client.listConsumerGroupOffsets(group);

            Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get();
//            Collection<OffsetAndMetadata> values = topicPartitionOffsetAndMetadataMap.values();

            Set<TopicPartition> topicPartitions = topicPartitionOffsetAndMetadataMap.keySet();

            Set<String> topics = topicPartitions.stream().map(t -> t.topic()).collect(Collectors.toSet());
            KafkaConsumer<String, String> consumer = getConsumer(sourceInfo, topics, group, "earliest");
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);

            List<JSONObject> collect = topicPartitions.stream().map(t -> {
                        OffsetAndMetadata offsetAndMetadata = topicPartitionOffsetAndMetadataMap.get(t);
                        long offset = offsetAndMetadata.offset();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("topic", t.topic());
                        jsonObject.put("partition", t.partition());
                        jsonObject.put("offset", offset);
                        TopicPartition topicPartition = new TopicPartition(t.topic(), t.partition());
                        Long endOffset = endOffsets.get(topicPartition);
                        jsonObject.put("endOffset", endOffset);
                        jsonObject.put("lag", endOffset - offset);
                        return jsonObject;
                    }).sorted(Comparator.comparing(KafkaUtil::comparingByName).thenComparing(KafkaUtil::comparingByPartition))
                    .collect(Collectors.toList());


            return ResponseDto.success(collect);
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            client.close();
        }

    }

    private static String comparingByName(JSONObject jo) {
        return jo.getString("topic");
    }

    private static Integer comparingByPartition(JSONObject jo) {
        return jo.getInteger("partition");
    }

    public static ResponseDto deleteGroup(SourceInfo sourceInfo, String group) {
        AdminClient client = null;
        try {
            client = createAdminClientByProperties(sourceInfo);
            List<String> list = new ArrayList<>();
            list.add(group);
            DeleteConsumerGroupsResult deleteConsumerGroupsResult = client.deleteConsumerGroups(list);
            Void aVoid = deleteConsumerGroupsResult.all().get();
            return ResponseDto.success();
        } catch (Exception e) {
            return ResponseDto.fail(e.getMessage());
        } finally {
            client.close();
        }

    }

}
