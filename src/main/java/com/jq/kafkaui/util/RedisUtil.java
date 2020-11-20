
package com.jq.kafkaui.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class RedisUtil {

    public Jedis getClient(String ip, int port, String password, int db) {
        JedisPoolConfig jcon = new JedisPoolConfig();
        jcon.setMaxTotal(200);
        jcon.setMaxIdle(50);
        jcon.setTestOnBorrow(true);
        jcon.setTestOnReturn(true);
        JedisPool jp = new JedisPool(jcon, ip, port, 100, password, db);
        Jedis jedis = jp.getResource();
        return jedis;
    }

    public Set<String> getAllKeys(Jedis jedis) {
        Set<String> keys = jedis.keys("*");
        return keys;
    }

    public String getString(Jedis jedis, String key) {
        String value = jedis.get(key);
        return value;
    }

    public Map<String, String> getHash(Jedis jedis, String key) {
        Map<String, String> map = jedis.hgetAll(key);
        return map;
    }

    public Set<String> getSet(Jedis jedis, String key) {
        return jedis.smembers(key);
    }

    public List<String> getList(Jedis jedis, String key) {
        return jedis.lrange(key, 0, -1);
    }

    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getClient("192.168.33.201", 6379, "123456", 6);
        redisUtil.getAllKeys(jedis);
//        jedis.lpush("list","aa");
//        jedis.lpush("list","bb");
//        jedis.lrange("list", 0, -1);
    }

}