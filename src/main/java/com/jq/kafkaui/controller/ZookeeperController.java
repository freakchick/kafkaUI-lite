package com.jq.kafkaui.controller;

import com.alibaba.fastjson.JSONObject;
import com.jq.kafkaui.domain.RedisSource;
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
    public List<JSONObject> getAllNodes(String address){
        return zkService.getAllNodes(address);
    }

    @RequestMapping("/getRootNodes")
    public List<JSONObject> getRootNodes(String address) throws Exception {
        return zkService.getRootNodes(address);
    }

    @RequestMapping("/getNodes")
    public List<JSONObject> getNodes(String address,String path) throws Exception {
        return zkService.getNodes(address,path);
    }

    @RequestMapping("/getData")
    public String getData(String address,String path){
        return zkService.getData(address,path);
    }

    @RequestMapping("/connect")
    public boolean connect(String address) {
        boolean connect = zkService.connect(address);
        return connect;
    }

}
