package com.jq.kafkaui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.ZKSource;
import com.jq.kafkaui.service.ZKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: kafkaUI
 * @description:
 * @author: jiangqiang
 * @create: 2020-11-13 15:26
 **/
@RestController
@RequestMapping("/zookeeper")
public class ZookeeperController {

    @Autowired
    ZKService zkService;

    @RequestMapping("/getAllSource")
    public List<ZKSource> getAllSource() {
        return zkService.getAllSource();
    }

    @RequestMapping("/getAllSourceAuth")
    public List<ZKSource> getAllSourceAuth() {
        return zkService.getAllSourceAuth();
    }

    @RequestMapping("/deleteSource/{id}")
    public String deleteSource(@PathVariable Integer id) {
        zkService.deleteSource(id);
        return "success";
    }

    @RequestMapping("/add")
    public String addSource(ZKSource source) {
        zkService.addSource(source);
        return "success";
    }

    @RequestMapping("/getAllNodes")
    public List<JSONObject> getAllNodes(Integer sourceId) {
        String address = zkService.getAddressById(sourceId);
        return zkService.getAllNodes(address);
    }

    @RequestMapping("/getRootNodes")
    public List<JSONObject> getRootNodes(Integer sourceId) throws Exception {
        String address = zkService.getAddressById(sourceId);
        return zkService.getRootNodes(address);
    }

    @RequestMapping("/getNodes")
    public List<JSONObject> getNodes(Integer sourceId, String path) throws Exception {
        String address = zkService.getAddressById(sourceId);
        return zkService.getNodes(address, path);
    }

    @RequestMapping("/getData")
    public String getData(Integer sourceId, String path) {
        
        String address = zkService.getAddressById(sourceId);
        return zkService.getData(address, path);
    }

    @RequestMapping("/setData")
    public boolean setData(Integer sourceId, String path, String data) throws Exception {
        String address = zkService.getAddressById(sourceId);
        zkService.setData(address, path, data);
        return true;
    }

    @RequestMapping("/createNode")
    public boolean createNode(Integer sourceId, String path, String data, Boolean recursion) throws Exception {
        String address = zkService.getAddressById(sourceId);
        zkService.createNode(address, path, data, recursion);
        return true;
    }

    @RequestMapping("/removeNode")
    public boolean createNode(Integer sourceId, String path) throws Exception {
        String address = zkService.getAddressById(sourceId);
        zkService.removeNode(address, path);
        return true;
    }

    @RequestMapping("/connect")
    public boolean connect(String address) {
//        String address = zkService.getAddressById(sourceId);
        boolean connect = zkService.connect(address);
        return connect;
    }

    @RequestMapping("/auth")
    public void auth(String param) throws Exception {
        zkService.auth(param);
    }

}
