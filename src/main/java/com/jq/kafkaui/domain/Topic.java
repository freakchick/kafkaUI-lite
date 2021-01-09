package com.jq.kafkaui.domain;

import lombok.Data;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-30 10:02
 **/
@Data
public class Topic {

    String name;
    boolean isInternal;
}
