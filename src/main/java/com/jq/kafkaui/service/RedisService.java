package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.RedisSourceDao;
import com.jq.kafkaui.domain.RedisSource;
import com.jq.kafkaui.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-12 17:39
 **/
@Service
public class RedisService {

    @Autowired
    RedisSourceDao sourceDao;

    public void addSource(RedisSource source) {
        sourceDao.insert(source);
    }

    public void deleteSource(Integer id) {
        sourceDao.delete(id);
    }

    public List<RedisSource> getAllSource() {
        return sourceDao.getAll();
    }

    public Set<String> getAllKeys(Integer sourceId, int db) {
        RedisSource redisSource = sourceDao.selectById(sourceId);

        RedisUtil redisPool = new RedisUtil();
        Jedis client = redisPool.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), db);
        Set<String> allKeys = redisPool.getAllKeys(client);
        client.close();
        return allKeys;
    }

    public JSONObject getData(Integer sourceId, Integer db, String key) {
        JSONObject jo = new JSONObject();

        RedisSource redisSource = sourceDao.selectById(sourceId);

        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), db);
        String type = jedis.type(key);
        jo.put("type", type);

        if (type.equalsIgnoreCase("string")) {
            String data = jedis.get(key);
            jo.put("value", data);

        } else if (type.equalsIgnoreCase("hash")) {
            Map<String, String> data = jedis.hgetAll(key);
            List<JSONObject> collect = data.keySet().stream().map(t -> {
                JSONObject object = new JSONObject();
                object.put("key", t);
                object.put("value", data.get(t));
                return object;
            }).collect(Collectors.toList());
            jo.put("value", data);

        } else if (type.equalsIgnoreCase("list")) {
            List<String> data = jedis.mget(key);
            jo.put("value", data);

        } else if (type.equalsIgnoreCase("set")) {
            Set<String> data = jedis.smembers(key);
            jo.put("value", data);

        }
        jedis.close();
        return jo;

    }
}
