package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.ZKSourceDao;
import com.jq.kafkaui.domain.ZKSource;
import com.jq.kafkaui.util.ZKProcessor;
import org.apache.curator.framework.CuratorFramework;
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
}
