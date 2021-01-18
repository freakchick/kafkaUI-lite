package com.jq.kafkaui.dto;

import lombok.Data;

@Data
public class ResponseDto {
    boolean success;
    String message;
    Object data;

    public static ResponseDto fail(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(false);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto success(String message, Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto success(Object data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setSuccess(true);
        responseDto.setData(data);
        return responseDto;
    }
}
