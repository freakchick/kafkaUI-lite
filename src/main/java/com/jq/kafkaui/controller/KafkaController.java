package com.jq.kafkaui.controller;

import com.jq.kafkaui.domain.Source;
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
@RequestMapping("/api")
public class KafkaController {

    @Autowired
    KafkaService kafkaService;

    @RequestMapping("/getTopics")
    public List<Topic> getTopics(String brokers) {
        List<Topic> list = KafkaUtil.listTopicsWithOptions(brokers);
        return list;
    }

    @RequestMapping("/getIp")
    public String getIpAndPort() {
        return kafkaService.getIpAndPort();
    }

    @RequestMapping("/getSource")
    public List<Source> getSource() {
        return kafkaService.getAllSource();
    }

    @RequestMapping("/deleteSource/{id}")
    public String deleteSource(@PathVariable Integer id) {
        kafkaService.deleteSource(id);
        return "success";
    }

    @RequestMapping("/createTopic")
    public String createTopic(String broker, String name,
                              @RequestParam(defaultValue = "1") Integer partition,
                              @RequestParam(defaultValue = "1") Integer replica) {
        try {
            KafkaUtil.createTopic(broker, name, partition, replica);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping("/add")
    public String addSource(Source source) {
        kafkaService.add(source);
        return "success";
    }

    @RequestMapping("/produce")
    public String addSource(String broker, String topic, String message) throws ExecutionException, InterruptedException {
        Producer<String, String> producer = KafkaUtil.getProducer(broker);
        Future<RecordMetadata> send = producer.send(new ProducerRecord<>(topic, message));
        send.get();
        return "success";
    }
}
