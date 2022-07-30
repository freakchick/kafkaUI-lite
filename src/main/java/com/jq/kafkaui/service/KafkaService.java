package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.KafkaSourceDao;
import com.jq.kafkaui.domain.Auth;
import com.jq.kafkaui.domain.KafkaSource;
import com.jq.kafkaui.dto.SourceInfo;
import com.jq.kafkaui.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class KafkaService {
    @Value("${server.port}")
    String port;

    @Autowired
    KafkaSourceDao kafkaSourceDao;

    public List<KafkaSource> getAllSource() {
        return kafkaSourceDao.getAll();
    }

    public List<KafkaSource> getAllSourceAuth() {
        List<KafkaSource> list = kafkaSourceDao.getAll();
        list.stream().forEach(t -> {
            Auth auth = kafkaSourceDao.getAuthBySource(t.getId());
            JSONObject authO = new JSONObject();
            authO.put("add", auth.getAdd_auth().intValue() == 1 ? true : false);
            authO.put("update", auth.getUpdate_auth().intValue() == 1 ? true : false);
            authO.put("remove", auth.getRemove_auth().intValue() == 1 ? true : false);
            t.setAuth(authO);
        });
        return list;

    }

//    public String getIpAndPort(HttpServletRequest request) {
//        // 通过命令行读取host参数 java -Dhost=192.168.33.201 -jar kafkaUI.jar
//        String ip = System.getProperty("host");
//        if (ip == null)
////            ip = IPUtil.getIpAddress();
//            ip = request.getServerName();
//        return ip + ":" + port;
//
//    }
    @Transactional
    public void add(KafkaSource source) {
        kafkaSourceDao.insert(source);

        kafkaSourceDao.insertAuth(source.getId(), 0, 0, 0);
    }

    @Transactional
    public void deleteSource(Integer id) {
        kafkaSourceDao.delete(id);
        kafkaSourceDao.deleteAuth(id);
    }

    @Transactional
    public void auth(String param) {
        JSONObject jo = JSON.parseObject(param);
        Set<String> keys = jo.keySet();
        keys.stream().forEach(key -> {
            JSONObject auth = jo.getJSONObject(key);
            int add = auth.getBoolean("add") ? 1 : 0;
            int update = auth.getBoolean("update") ? 1 : 0;
            int remove = auth.getBoolean("remove") ? 1 : 0;
            int i = kafkaSourceDao.updateAuth(Integer.parseInt(key), add, update, remove);
        });

    }

    public SourceInfo getSourceInfo(Integer sourceId) {
        return kafkaSourceDao.selectById(sourceId);
    }
}
