package com.jq.kafkaui.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class KafkaSource {
    Integer id;
    String name;
    String broker;
    String password;
    String username;

    JSONObject auth;
}
