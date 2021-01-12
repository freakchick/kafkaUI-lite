package com.jq.kafkaui.domain;

import lombok.Data;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-12 14:15
 **/
@Data
public class Result {
    boolean success;
    Object data;
    String message;

    public static Result fail(String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    public static Result success(String message) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

}
