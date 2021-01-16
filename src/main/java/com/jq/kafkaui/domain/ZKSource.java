package com.jq.kafkaui.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-13 15:27
 **/
@Data
public class ZKSource {
    Integer id;
    String name;
    String address;

    JSONObject auth;
}
