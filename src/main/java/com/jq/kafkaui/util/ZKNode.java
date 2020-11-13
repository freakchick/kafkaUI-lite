package com.jq.kafkaui.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ZKNode {

    private StringProperty name;
    private StringProperty path;
    private StringProperty value;

    public ZKNode(String name, String path, String value) {
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(path);
        this.value = new SimpleStringProperty(value);
    }

    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPath() {
        return path.getValue();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getValue() {
        return value.getValue();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
