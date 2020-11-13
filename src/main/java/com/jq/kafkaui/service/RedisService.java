package com.jq.kafkaui.service;

import com.jq.kafkaui.dao.RedisSourceDao;
import com.jq.kafkaui.domain.RedisSource;
import com.jq.kafkaui.util.RedisPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

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

        RedisPool redisPool = new RedisPool();
        Jedis client = redisPool.getClient(redisSource.getIp(), redisSource.getPort(), redisSource.getPassword(), db);
        Set<String> allKeys = redisPool.getAllKeys(client);
        return allKeys;
    }
}
