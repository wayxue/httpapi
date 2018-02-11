package com.yitaqi.gate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * 入口配置
 * ComponentScan(basePackages)告诉spring 注册该包下的类
 * ServletComponentScan 使用servlet 注解，配合WebServlet 使用
 * @author xue
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yitaqi.controller", "com.yitaqi.service", "com.yitaqi.core"})
@MapperScan(basePackages = "com.yitaqi.dao")
@ServletComponentScan(basePackages = "com.yitaqi.servlet")
public class GateApplication {

	public static void main(String[] args) {

		// 调用servlet

		SpringApplication.run(GateApplication.class, args);
	}
}
