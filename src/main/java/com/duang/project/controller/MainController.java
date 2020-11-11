package com.duang.project.controller;

import cn.hutool.core.util.IdUtil;
import duang.mvc.common.annotation.Controller;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.core.BaseController;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Controller
@Mapping(value = "/main", desc = "主控制器")
public class MainController extends BaseController {

    @Mapping(value = "/findById", desc = "根据ID查询")
    public String findById(@NotEmpty(message = "id字段不能为空") String id,
                               @Max(value = 2, message = "最大值不能大于2") Integer age) {
        System.out.println(id);
        System.out.println(age);
        return IdUtil.objectId();
    }

    public String get(MainEntity mainEntity) {
        System.out.println("########:" + mainEntity);
        return IdUtil.objectId();
    }

    public String file() {
        try {
            getRequest().getFile();
            return IdUtil.objectId();
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
