package com.jq.kafkaui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.KafkaSource;
import com.jq.kafkaui.domain.Topic;
import com.jq.kafkaui.service.KafkaService;
import com.jq.kafkaui.util.KafkaUtil;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-28 20:06
 **/
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    KafkaService kafkaService;

    @RequestMapping("/getTopics")
    public List<Topic> getTopics(Integer sourceId) throws Exception {
        String brokers = kafkaService.getBroker(sourceId);
        List<Topic> list = KafkaUtil.listTopicsWithOptions(brokers);
        return list;
    }

    @RequestMapping("/getIp")
    public String getIpAndPort() {
        return kafkaService.getIpAndPort();
    }

    @RequestMapping("/getSource")
    public List<KafkaSource> getAllSource() {
        return kafkaService.getAllSource();
    }

    @RequestMapping("/getAllSourceAuth")
    public List<KafkaSource> getSourceAuth() {
        return kafkaService.getAllSourceAuth();
    }

    @RequestMapping("/deleteSource/{id}")
    public String deleteSource(@PathVariable Integer id) {
        kafkaService.deleteSource(id);
        return "success";
    }

    @RequestMapping("/add")
    public String addSource(KafkaSource source) {
        kafkaService.add(source);
        return "success";
    }

    @RequestMapping("/createTopic")
    public String createTopic(Integer sourceId, String name,
                              @RequestParam(defaultValue = "1") Integer partition,
                              @RequestParam(defaultValue = "1") Integer replica) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        KafkaUtil.createTopic(broker, name, partition, replica);
        return "success";

    }

    @RequestMapping("/deleteTopic")
    public boolean deleteTopic(Integer sourceId, String topic) {
        String broker = kafkaService.getBroker(sourceId);
        KafkaUtil.deleteTopic(broker, topic);
        return true;

    }

    @RequestMapping("/getTopicDetail")
    public JSONObject getTopicDetail(Integer sourceId, String topic) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        return KafkaUtil.getTopicDetail(broker, topic);
    }

    @RequestMapping("/produce")
    public String produce(Integer sourceId, String topic, String message, Boolean batch) throws ExecutionException, InterruptedException {
        String broker = kafkaService.getBroker(sourceId);
        Producer<String, String> producer = KafkaUtil.getProducer(broker);
        if (batch) {
            String[] messages = message.split("\n");
            for (String ms : messages) {
                Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, message));
                send.get();
            }
        } else {
            Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, message));
            send.get();
        }
        producer.close();
        return "success";
    }

    @RequestMapping("/cluster/info")
    public List<JSONObject> getClusterInfo(Integer sourceId) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        List<JSONObject> collection = KafkaUtil.clusterInfo(broker);
        return collection;
    }

    @RequestMapping("/group/all")
    public List<JSONObject> getAllGroups(Integer sourceId) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        List<JSONObject> allGroups = KafkaUtil.getAllGroups(broker);
        return allGroups;
    }

    @RequestMapping("/group/detail")
    public Collection<List<JSONObject>> getGroupDetail(Integer sourceId, String group) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        Collection<List<JSONObject>> groupInfo = KafkaUtil.getGroupInfo(broker, group);
        return groupInfo;
    }

    @RequestMapping("/group/delete")
    public void deleteGroup(Integer sourceId, String group) throws Exception {
        String broker = kafkaService.getBroker(sourceId);
        KafkaUtil.deleteGroup(broker, group);
    }

    @RequestMapping("/auth")
    public void auth(String param) throws Exception {
        kafkaService.auth(param);
    }
}
