package com.yitaqi.controller;

import com.yitaqi.core.ApiGateHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器
 * @author xue
 */
@RestController
public class GateController implements ApplicationListener<ContextRefreshedEvent> {


    private ApiGateHandler handler;

    @RequestMapping("/")
    public String welcome() {

        return "have a nice day!";
    }

    /**
     * API 网关接口设置
     * get 请求路径上参数含有{}时，会出现 400 ： bad request
     * 解决方法1：将特殊字符{}转义，{：%7B，}：%7D
     * 如： http://localhost:8080/ser?method=123&params=%7B%7D
     */
    @RequestMapping("/api")
    public void api(HttpServletRequest request, HttpServletResponse response) {

        handler.handle(request, response);

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        handler = contextRefreshedEvent.getApplicationContext().getBean(ApiGateHandler.class);
        System.out.println("web is startup");
    }

}
