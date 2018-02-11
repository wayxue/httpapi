package com.yitaqi.controller;

import com.yitaqi.gate.GateApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GateApplication.class)
@AutoConfigureMockMvc
public class GateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void welcome() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/");
        ResultActions resultActions = mvc.perform(builder);
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String res = response.getContentAsString();
        System.out.println(res);

    }

    @Test
    public void apiTest() throws Exception {

        // 在此处测试没问题，将这个路径放到浏览器中，会出现 400 bad request 错误
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api?method=123&params={}");
        ResultActions resultActions = mvc.perform(builder);
        MvcResult mvcResult = resultActions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String res = response.getContentAsString();
        System.out.println(res);
    }

}