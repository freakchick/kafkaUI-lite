package com.jq.kafkaui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-10-26 18:03
 **/
@EnableAsync
@SpringBootApplication
public class KafkaUIApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaUIApplication.class, args);
    }
}
