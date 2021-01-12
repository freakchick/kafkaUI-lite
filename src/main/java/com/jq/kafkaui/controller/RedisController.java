package com.jq.kafkaui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.RedisSource;
import com.jq.kafkaui.domain.Result;
import com.jq.kafkaui.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-12 17:38
 **/
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisService redisService;

    @RequestMapping("/getAllSource")
    public List<RedisSource> getAllSource() {
        return redisService.getAllSource();
    }

    @RequestMapping("/deleteSource/{id}")
    public String deleteSource(@PathVariable Integer id) {
        redisService.deleteSource(id);
        return "success";
    }

    @RequestMapping("/add")
    public String addSource(RedisSource source) {
        redisService.addSource(source);
        return "success";
    }

    @RequestMapping("/getAllKeys")
    public Set<String> getAllKeys(Integer sourceId, Integer db) {
        return redisService.getAllKeys(sourceId, db);
    }

    @RequestMapping("/getData")
    public JSONObject getData(Integer sourceId, Integer db, String key) {
        JSONObject data = redisService.getData(sourceId, db, key);
        return data;
    }

    @RequestMapping("/connect")
    public boolean connect(RedisSource source) {
        boolean connect = redisService.connect(source);
        return connect;
    }

    @RequestMapping("/addKey")
    public Result addKey(Integer sourceId, Integer db, String key, String type, String value) {
        try {

            Result result = redisService.addKey(sourceId, db, key, type, value);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    @RequestMapping("/deleteKey")
    public void deleteKey(Integer sourceId, Integer db, String key) {
        redisService.deleteKey(sourceId, db, key);
    }
}
