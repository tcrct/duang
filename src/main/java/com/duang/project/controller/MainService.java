package com.duang.project.controller;

import duang.mvc.common.annotation.Service;
import duang.utils.DuangId;

@Service
public class MainService {
    public String index(String msg) {
        return DuangId.get().toString()+"_" + msg;
    }
}
