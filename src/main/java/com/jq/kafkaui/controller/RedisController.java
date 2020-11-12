package com.jq.kafkaui.controller;

import com.jq.kafkaui.domain.RedisSource;
import com.jq.kafkaui.domain.Source;
import com.jq.kafkaui.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-12 17:38
 **/

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    @RequestMapping("/addSource")
    public String addSource(RedisSource source) {
        redisService.add(source);
        return "success";
    }
}
