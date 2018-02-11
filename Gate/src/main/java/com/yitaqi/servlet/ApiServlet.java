package com.yitaqi.servlet;

import com.yitaqi.core.ApiGateHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xue
 */
@WebServlet(urlPatterns = "/ser")
public class ApiServlet extends HttpServlet {

    ApiGateHandler handler;

    @Override
    public void init() throws ServletException {

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        handler = (ApiGateHandler) context.getBean(ApiGateHandler.class);
        System.out.println("i am a servlet named wubai");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.handle(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
