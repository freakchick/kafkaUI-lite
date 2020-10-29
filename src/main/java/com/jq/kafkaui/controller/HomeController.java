package com.jq.kafkaui.controller;

import com.jq.kafkaui.conf.WebSocketServer;
import com.jq.kafkaui.util.KafkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
    WebSocketServer webSocketServer;

    @RequestMapping("/getTopics")
    public List<String> getTopics(String brokers){
        List<String> list = KafkaUtil.listTopicsWithOptions(brokers);
        return list;
    }

    @RequestMapping("/sendAllWebSocket")
    public String test() {
        String text="你们好！这是websocket群体发送！";
        try {
            webSocketServer.sendInfo(text);
        }catch (IOException e){
            e.printStackTrace();
        }
        return text;
    }


}
