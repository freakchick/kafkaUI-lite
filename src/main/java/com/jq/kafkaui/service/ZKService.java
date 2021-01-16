package com.jq.kafkaui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.dao.ZKSourceDao;
import com.jq.kafkaui.domain.Auth;
import com.jq.kafkaui.domain.ZKSource;
import com.jq.kafkaui.util.ZKProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Transactional
    public void addSource(ZKSource source) {
        sourceDao.insert(source);
        sourceDao.insertAuth(source.getId(), 0, 0, 0);
    }

    @Transactional
    public void deleteSource(Integer id) {
        sourceDao.delete(id);
        sourceDao.deleteAuth(id);
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
        client.close();
        return allSon;
    }

    public List<JSONObject> getNodes(String address, String path) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        CuratorFramework client = zkProcessor.getClient();
        List<JSONObject> allSon = zkProcessor.getAllSon(client, path);
        client.close();
        return allSon;
    }

    public boolean connect(String address) {
        CuratorFramework client = null;
        try {
            ZKProcessor zkProcessor = new ZKProcessor(address);
            client = zkProcessor.getClient();
            List<JSONObject> allSon = zkProcessor.getAllSon(client, "/");
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            client.close();
        }
    }

    public void setData(String address, String path, String data) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        zkProcessor.setValue(path, data);
    }

    public void createNode(String address, String path, String data, boolean recursion) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        zkProcessor.createNode(path, data, recursion);
    }

    public void removeNode(String address, String path) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor(address);
        zkProcessor.removeNode(path);
    }

    public List<ZKSource> getAllSourceAuth() {
        List<ZKSource> all = sourceDao.getAll();
        all.stream().forEach(t -> {
            Auth auth = sourceDao.getAuthBySource(t.getId());
            JSONObject authO = new JSONObject();
            authO.put("add", auth.getAdd_auth().intValue() == 1 ? true : false);
            authO.put("update", auth.getUpdate_auth().intValue() == 1 ? true : false);
            authO.put("remove", auth.getRemove_auth().intValue() == 1 ? true : false);
            t.setAuth(authO);
        });
        return all;
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
            int i = sourceDao.updateAuth(Integer.parseInt(key), add, update, remove);
        });

    }

    public String getAddressById(Integer sourceId) {
        return sourceDao.getAddress(sourceId);
    }
}
