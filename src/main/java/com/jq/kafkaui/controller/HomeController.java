package com.jq.kafkaui.controller;

import com.jq.kafkaui.domain.Source;
import com.jq.kafkaui.service.KafkaService;
import com.jq.kafkaui.util.KafkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-28 20:06
 **/
@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    KafkaService kafkaService;

    @RequestMapping("/getTopics")
    public List<String> getTopics(String brokers) {
        List<String> list = KafkaUtil.listTopicsWithOptions(brokers);
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

    @RequestMapping("/add")
    public String addSource(Source source) {
         kafkaService.add(source);
         return "success";
    }
}
