
package com.jq.kafkaui.util;

import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisUtil {

    public Jedis getClient(String ip, int port, String password, int db) {
//        JedisPoolConfig jcon = new JedisPoolConfig();
//        jcon.setMaxTotal(200);
//        jcon.setMaxIdle(50);
//        jcon.setTestOnBorrow(true);
//        jcon.setTestOnReturn(true);
//        JedisPool jp = new JedisPool(jcon, ip, port, 100, password, db);
//        Jedis jedis = jp.getResource();
        Jedis jedis = new Jedis(ip, port);

        if (!StringUtils.isEmpty(password)) {
            String auth = jedis.auth(password);
            System.out.println(auth);
        }
        String select = jedis.select(db);
        System.out.println(select);
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

    public void hashSet(Jedis jedis, String key, Map<String, String> map) {
        jedis.hmset(key, map);
    }

    public void setSet(Jedis jedis, String key, List<String> list) {
        String[] array = list.toArray(new String[list.size()]);
        jedis.sadd(key, array);
    }

    public void listSet(Jedis jedis, String key, List<String> list) {
        String[] array = list.toArray(new String[list.size()]);
        jedis.rpush(key, array);
    }

    public void closeConnction(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getClient("10.1.254.38", 6379, "123456", 6);
        List<String> test3 = redisUtil.getList(jedis, "test3");
        test3.forEach(t -> {
            System.out.println(t);
        });
//        jedis.lpush("list","aa");
//        jedis.lpush("list","bb");
//        jedis.lrange("list", 0, -1);
    }

}