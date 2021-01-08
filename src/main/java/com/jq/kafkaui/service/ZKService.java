package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.ZKSourceDao;
import com.jq.kafkaui.domain.ZKSource;
import com.jq.kafkaui.util.ZKProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-13 15:30
 **/
@Service
@Slf4j
public class ZKService {

    @Autowired
    ZKSourceDao sourceDao;

    public void addSource(ZKSource source) {
        sourceDao.insert(source);
    }

    public void deleteSource(Integer id) {
        sourceDao.delete(id);
    }

    public List<ZKSource> getAllSource() {
        return sourceDao.getAll();
    }

    public List<JSONObject> getAllNodes(String address) {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        return zkProcessor.getAllNode();
    }

    public String getData(String address, String path) {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        return zkProcessor.getValue(path);
    }

    public List<JSONObject> getRootNodes(String address) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        CuratorFramework client = zkProcessor.getClient();
        List<JSONObject> allSon = zkProcessor.getAllSon(client, "/");
        return allSon;
    }

    public List<JSONObject> getNodes(String address, String path) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        CuratorFramework client = zkProcessor.getClient();
        List<JSONObject> allSon = zkProcessor.getAllSon(client, path);
        return allSon;
    }

    public boolean connect(String address) {
        CuratorFramework client = null;
        try {
            ZKProcessor zkProcessor = new ZKProcessor(address);
            client = zkProcessor.getClient();
            ExistsBuilder existsBuilder = client.checkExists();
            CuratorFrameworkState state = client.getState();
            List<JSONObject> allSon = zkProcessor.getAllSon(client, "/");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            client.close();
        }
    }
}
