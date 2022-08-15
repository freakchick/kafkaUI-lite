package com.jq.kafkaui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.KafkaSource;
import com.jq.kafkaui.dto.ResponseDto;
import com.jq.kafkaui.dto.SourceInfo;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
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
    public ResponseDto getTopics(Integer sourceId) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        return KafkaUtil.listTopicsWithOptions(sourceInfo, null);
    }

    @RequestMapping("/getIp")
    public String getIpAndPort(HttpServletRequest request) {
        //获取浏览器访问地址中的ip和端口，防止容器运行时候产生问题
        return request.getServerName() + ":" + request.getServerPort();
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

    @RequestMapping("/getBroker")
    public String getBroker(Integer sourceId) {
        return Optional.ofNullable(kafkaService.getSourceInfo(sourceId))
                .map(SourceInfo::getBroker).orElse(null);
    }

    @RequestMapping("/createTopic")
    public String createTopic(Integer sourceId, String name,
                              @RequestParam(defaultValue = "1") Integer partition,
                              @RequestParam(defaultValue = "1") Integer replica) throws Exception {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        KafkaUtil.createTopic(sourceInfo, name, partition, replica);
        return "success";

    }

    @RequestMapping("/deleteTopic")
    public boolean deleteTopic(Integer sourceId, String topic) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        KafkaUtil.deleteTopic(sourceInfo, topic);
        return true;

    }

    @RequestMapping("/searchTopic")
    public ResponseDto searchTopic(Integer sourceId, String topic) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        ResponseDto responseDto = KafkaUtil.listTopicsWithOptions(sourceInfo, topic);
        return responseDto;

    }

    @RequestMapping("/getTopicDetail")
    public JSONObject getTopicDetail(Integer sourceId, String topic) throws Exception {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        return KafkaUtil.getTopicDetail(sourceInfo, topic);
    }

    @RequestMapping("/produce")
    public String produce(Integer sourceId, String topic, String message, Boolean batch) throws ExecutionException, InterruptedException {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        Producer<String, String> producer = KafkaUtil.getProducer(sourceInfo);
        if (batch) {
            String[] messages = message.split("\n");
            for (String ms : messages) {
                Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, ms));
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
    public ResponseDto getClusterInfo(Integer sourceId) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        return KafkaUtil.clusterInfo(sourceInfo);
    }

    @RequestMapping("/group/all")
    public ResponseDto getAllGroups(Integer sourceId) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        ResponseDto allGroups = KafkaUtil.getAllGroups(sourceInfo, null);
        return allGroups;
    }

    @RequestMapping("/group/search")
    public ResponseDto getAllGroups(Integer sourceId, String keyword) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        ResponseDto allGroups = KafkaUtil.getAllGroups(sourceInfo, keyword);
        return allGroups;
    }

    @RequestMapping("/group/detail")
    public ResponseDto getGroupDetail(Integer sourceId, String group) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        ResponseDto groupInfo = KafkaUtil.getGroupInfo(sourceInfo, group);
        return groupInfo;
    }

    @RequestMapping("/group/delete")
    public ResponseDto deleteGroup(Integer sourceId, String group) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        return KafkaUtil.deleteGroup(sourceInfo, group);
    }

    @RequestMapping("/auth")
    public void auth(String param) throws Exception {
        kafkaService.auth(param);
    }


    @RequestMapping("/getGroupsByTopic")
    public ResponseDto getGroupByTopic(Integer sourceId, String topic) {
        SourceInfo sourceInfo = kafkaService.getSourceInfo(sourceId);
        return KafkaUtil.getGroupByTopic(sourceInfo, topic);
    }
}
