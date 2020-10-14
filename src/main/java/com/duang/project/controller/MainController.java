package com.duang.project.controller;

import cn.hutool.core.util.IdUtil;
import duang.mvc.common.annotation.Controller;
import duang.mvc.common.annotation.Mapping;

@Controller
@Mapping(value = "/main", desc = "主控制器")
public class MainController {

    @Mapping(value = "/findById", desc = "根据ID查询")
    public String findById(String id) {
        return IdUtil.objectId();
    }

    public String get() {
        return IdUtil.objectId();
    }

}
