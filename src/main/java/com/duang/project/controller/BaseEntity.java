package com.duang.project.controller;

public abstract class BaseEntity implements java.io.Serializable {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
