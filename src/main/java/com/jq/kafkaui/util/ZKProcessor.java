package com.jq.kafkaui.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.scene.control.TreeItem;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.ArrayList;
import java.util.List;

public class ZKProcessor {

    String url;

    public ZKProcessor(String url) {
        this.url = url;
    }

    /**
     * 获取zk客户端
     *
     * @return
     */
    public CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(url)
                .sessionTimeoutMs(1000)  // 会话超时时间
                .connectionTimeoutMs(1000) // 连接超时时间
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

    //获取zk一个节点下的所有子孙节点
    public List<JSONObject> getAllChildren(CuratorFramework client, String path) throws Exception {

        List<JSONObject> list = new ArrayList<>();
        List<String> children = client.getChildren().forPath(path);
        for (String s : children) {
            JSONObject obj = new JSONObject(true);
            obj.put("label", s);
            String thisPath = path + (path.equals("/") ? "" : "/") + s;
            obj.put("path", thisPath);
//            obj.put("value", new String(client.getData().forPath(thisPath)));

            List<JSONObject> grandChildren = getAllChildren(client, thisPath);
            if (grandChildren.size() > 0)
                obj.put("children", grandChildren);
            list.add(obj);
        }
        return list;

    }

    //获取zk一个节点下的所有子节点
    public List<JSONObject> getAllSon(CuratorFramework client, String path) throws Exception {

        List<JSONObject> list = new ArrayList<>();
        List<String> children = client.getChildren().forPath(path);
        for (String s : children) {
            JSONObject obj = new JSONObject(true);
            obj.put("label", s);
            String thisPath = path + (path.equals("/") ? "" : "/") + s;
            obj.put("path", thisPath);
            byte[] bytes = client.getData().forPath(thisPath);
            if (bytes != null)
                obj.put("value", new String(bytes));
            list.add(obj);
        }

        return list;

    }

    //获取zk上的所有数据，返回json string
    public List<JSONObject> getAllNode() {
        CuratorFramework client = getClient();
        List<JSONObject> children = null;
        try {
            children = getAllChildren(client, "/");

            return children;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return null;
    }

    //删除zk上的某个节点
    public void removeNode(String path) throws Exception {

        CuratorFramework client = getClient();
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        client.close();
    }

    public String getValue(String path) {
        CuratorFramework client = getClient();
        try {

            byte[] bytes = client.getData().forPath(path);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.close();
        }
        return null;
    }

    public void setValue(String path, String data) throws Exception {
        CuratorFramework client = getClient();
        client.setData().forPath(path, data.getBytes("utf-8"));
        client.close();
    }

    public void createNode(String path, String data) throws Exception {
        CuratorFramework client = getClient();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes("utf-8"));
        client.close();
    }

    //递归调用
    public void get(JSONArray jsonArray, TreeItem<ZKNode> root) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String path = jsonObject.getString("path");
            String value = jsonObject.getString("value");
            ZKNode node = new ZKNode(name, path, value);
            TreeItem<ZKNode> item = new TreeItem<>(node);

            item.setExpanded(true);
            JSONArray children = jsonObject.getJSONArray("children");
            if (children != null && children.size() > 0) {
                get(children, item);
            }
            root.getChildren().add(item);
        }
    }

    public static void main(String[] args) throws Exception {
        ZKProcessor zkProcessor = new ZKProcessor("localhost:2181");

    }

}
