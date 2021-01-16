package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.RedisSourceDao;
import com.jq.kafkaui.domain.Auth;
import com.jq.kafkaui.domain.RedisSource;
import com.jq.kafkaui.domain.Result;
import com.jq.kafkaui.domain.ZKSource;
import com.jq.kafkaui.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
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
@Slf4j
public class RedisService {

    @Autowired
    RedisSourceDao sourceDao;

    public void addSource(RedisSource source) {
        sourceDao.insert(source);
        sourceDao.insertAuth(source.getId(), 0, 0, 0);
    }

    public void deleteSource(Integer id) {
        sourceDao.delete(id);
        sourceDao.deleteAuth(id);
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
            jo.put("value", collect);

        } else if (type.equalsIgnoreCase("list")) {
            List<String> data = redisUtil.getList(jedis, key);
            List<JSONObject> list = data.stream().map(t -> {
                JSONObject oo = new JSONObject();
                oo.put("value", t);
                return oo;
            }).collect(Collectors.toList());
            jo.put("value", list);

        } else if (type.equalsIgnoreCase("set")) {
            Set<String> data = redisUtil.getSet(jedis, key);
            List<JSONObject> list = data.stream().map(t -> {
                JSONObject oo = new JSONObject();
                oo.put("value", t);
                return oo;
            }).collect(Collectors.toList());
            jo.put("value", list);

        }
        redisUtil.closeConnction(jedis);
        return jo;

    }

    public boolean connect(RedisSource redisSource) {
        RedisUtil redisUtil = new RedisUtil();
        try {

            Jedis jedis = redisUtil.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), 0);
            jedis.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }

    }

    public Result addKey(Integer sourceId, Integer db, String key, String type, String value) {
        RedisSource redisSource = sourceDao.selectById(sourceId);
        RedisUtil redisUtil = new RedisUtil();

        Jedis jedis = redisUtil.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), db);
        if (jedis.exists(key)) {
            return Result.fail("key已存在，不可添加，添加可能覆盖数据");
        }
        if ("string".equals(type)) {
            jedis.set(key, value);
        } else if ("set".equals(type)) {
            List<JSONObject> list = JSON.parseArray(value, JSONObject.class);
            List<String> data = list.stream().map(t -> t.getString("value")).collect(Collectors.toList());
            redisUtil.setSet(jedis, key, data);
        } else if ("list".equals(type)) {
            List<JSONObject> list = JSON.parseArray(value, JSONObject.class);
            List<String> data = list.stream().map(t -> t.getString("value")).collect(Collectors.toList());
            redisUtil.listSet(jedis, key, data);
        } else if ("hash".equals(type)) {
            Map<String, String> map = new HashMap<>();
            List<JSONObject> list = JSON.parseArray(value, JSONObject.class);
            list.stream().forEach(t -> {
                map.put(t.getString("key"), t.getString("value"));
            });
            redisUtil.hashSet(jedis, key, map);
        }

        redisUtil.closeConnction(jedis);
        return Result.success("添加redis key 成功");
    }

    public void deleteKey(Integer sourceId, Integer db, String key) {
        RedisSource redisSource = sourceDao.selectById(sourceId);
        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), db);
        jedis.del(key);
        redisUtil.closeConnction(jedis);
    }

    public List<RedisSource> getAllSourceAuth() {
        List<RedisSource> all = sourceDao.getAll();
        all.stream().forEach(t -> {
            Auth auth = sourceDao.getAuthBySource(t.getId());
            JSONObject authO = new JSONObject();
            authO.put("add", auth.getAdd_auth().intValue() == 1 ? true : false);
            authO.put("update", auth.getUpdate_auth().intValue() == 1 ? true : false);
            authO.put("remove", auth.getRemove_auth().intValue() == 1 ? true : false);
            t.setAuth(authO);
        });
        return all;
    }

    @Transactional
    public void auth(String param) {
        JSONObject jo = JSON.parseObject(param);
        Set<String> keys = jo.keySet();
        keys.stream().forEach(key -> {
            JSONObject auth = jo.getJSONObject(key);
            int add = auth.getBoolean("add") ? 1 : 0;
            int update = auth.getBoolean("update") ? 1 : 0;
            int remove = auth.getBoolean("remove") ? 1 : 0;
            int i = sourceDao.updateAuth(Integer.parseInt(key), add, update, remove);
        });

    }
}
