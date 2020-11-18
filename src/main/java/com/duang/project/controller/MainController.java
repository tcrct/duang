package com.duang.project.controller;

import cn.hutool.core.util.IdUtil;
import duang.mvc.common.annotation.Controller;
import duang.mvc.common.annotation.Mapping;
import duang.mvc.common.core.BaseController;
import duang.mvc.websocket.WebSocketKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Controller
@Mapping(value = "/main", desc = "主控制器")
public class MainController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Mapping(value = "/findById", desc = "根据ID查询")
    public String findById(@NotEmpty(message = "id字段不能为空") String id,
                               @Max(value = 2, message = "最大值不能大于2") Integer age) {
        LOGGER.info("{}", id);
        LOGGER.info("{}", age);
        WebSocketKit.duang().key("/ws/main").message("findByid: " + id).send();
        return IdUtil.objectId();
    }

    public String get(MainEntity mainEntity) {
        System.out.println("########:" + mainEntity);
        return IdUtil.objectId();
    }

    public String file() {
        try {
            LOGGER.info(getRequest().params("name"));
            LOGGER.info(getRequest().params("size"));
            getUploadFiles();
            return IdUtil.objectId();
        }catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
