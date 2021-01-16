package com.jq.kafkaui.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-12 17:40
 **/
@Data
public class RedisSource {

    Integer id;
    String name;
    String ip;
    Integer port;
    Integer db;
    String password;

    JSONObject auth;
}
