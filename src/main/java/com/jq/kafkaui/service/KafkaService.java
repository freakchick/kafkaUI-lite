package com.jq.kafkaui.service;

import com.jq.kafkaui.dao.SourceDao;
import com.jq.kafkaui.domain.Source;
import com.jq.kafkaui.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaService {
    @Value("${server.port}")
    String port;

    @Autowired
    SourceDao sourceDao;

    public List<Source> getAllSource(){
        return sourceDao.getAll();
    }

    public String getIpAndPort() {
        // 通过命令行读取host参数 java -Dhost=192.168.33.201 -jar kafkaUI.jar
        String ip = System.getProperty("host");
        if (ip == null)
            ip = IPUtil.getIpAddress();
        return ip + ":" + port;

    }

}
