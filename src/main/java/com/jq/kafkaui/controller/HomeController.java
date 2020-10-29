package com.jq.kafkaui.controller;

import com.jq.kafkaui.util.KafkaUtil;
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

    @RequestMapping("/getTopics")
    public List<String> getTopics(String brokers){
        List<String> list = KafkaUtil.listTopicsWithOptions(brokers);
        return list;
    }


}
