package com.yitaqi.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yitaqi.core.ApiStore.ApiRunnable;
import com.yitaqi.exception.ApiException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 处理参数，调用对应的服务，返回结果
 * @author xue
 */
@Component
public class ApiGateHandler implements InitializingBean, ApplicationContextAware {

    private static String METHOD = "method";
    private static String PARAMS = "params";
    ApiStore apiStore;
    final ParameterNameDiscoverer parameterNameDiscoverer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        apiStore = new ApiStore(applicationContext);
    }

    public ApiGateHandler() {

        // 获取方法上变量的名称
        parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) {

        String method = request.getParameter(METHOD);
        String params = request.getParameter(PARAMS);
        Object result;
        ApiRunnable apiRunnable;
        try {
            apiRunnable = sysParamsValidate(method, params);
            Object[] args = buildParams(apiRunnable, params, request);
            result = apiRunnable.run(args);
        } catch (ApiException e) {
            response.setStatus(500);
            result = handleError(e);
        } catch (InvocationTargetException e) {
            response.setStatus(500);
            result = handleError(e.getTargetException());
        } catch (Exception e) {
            response.setStatus(500);
            result = handleError(e);
        }
        returnResult(result, response);
    }

    private ApiRunnable sysParamsValidate(String method, String params) {

        ApiRunnable apiRunnable;
        if (StringUtils.isEmpty(method)) {
            throw new ApiException("调用失败：参数'method'为空");
        } else if (params == null) {
            throw new ApiException("调用失败：参数'params'为空");
        } else if ((apiRunnable = apiStore.findApiRunnable(method, "")) == null) {
            throw new ApiException("调用失败：指定API不存在，API:" + method);
        }
        return apiRunnable;
    }

    private Object[] buildParams(ApiRunnable apiRunnable, String params, HttpServletRequest request) {

        Map<String, Object> map;
        try {
            map = (Map) JSON.parse(params);
        } catch (JSONException e) {
            throw new ApiException("调用失败：json 字符串格式异常，请检查 params 参数");
        }
        if (map == null) {
            map = new HashMap<>(1);
        }
        Method method = apiRunnable.targetMethod;
        List<String> parameterNames = Arrays.asList(parameterNameDiscoverer.getParameterNames(method));
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (!parameterNames.contains(m.getKey())) {
                throw new ApiException("调用失败：接口不存在'" + m.getKey() + "' 参数");
            }
        }
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i].isAssignableFrom(HttpServletRequest.class)) {
                args[i] = request;
            } else if (map.containsKey(parameterNames.get(i))) {
                try {
                    // fastjson对于基本类型无法解析，需要单独解析

                    args[i] = parseObject(map.get(parameterNames.get(i)).toString(), parameterTypes[i]);
                } catch (Exception e) {
                    throw new ApiException("调用失败：指定参数格式错误或值错误'" + parameterNames.get(i) + "'" + e.toString());
                }
            } else {
                args[i] = null;
            }
        }
        return args;
    }

    private Object parseObject(String value, Class<?> clazz) throws ParseException {

        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.parseInt(value);
        } else if (clazz == Double.class) {
            return Double.parseDouble(value);
        } else if (clazz == Float.class) {
            return Float.parseFloat(value);
        } else if (clazz == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == Long.class) {
            return Long.parseLong(value);
        } else if (clazz == Short.class) {
            return Short.parseShort(value);
        } else if (clazz == Date.class) {
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            return format.parse(value);
        } else if (clazz == BigDecimal.class) {
            return new BigDecimal(value);
        } else {
            return JSON.parseObject(value, clazz);
        }
    }

    private Object handleError(Throwable throwable) {

        String code;
        String message;

        if (throwable instanceof ApiException) {
            code = "0001";
            message = throwable.getMessage();
        } else {
            code = "0002";
            message = throwable.toString();
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("error", code);
        result.put("msg", message);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(outputStream);
        throwable.printStackTrace(stream);
        return result;
    }

    private void returnResult(Object result, HttpServletResponse response) {

        try {
            String json = JSON.toJSONString(result);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html/json;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "co-cache");
            response.setDateHeader("Expires", 0);
            if (!StringUtils.isEmpty(json)) {
                response.getWriter().write(json);
            }
        } catch (IOException e) {
            throw new RuntimeException("服务中心相应异常", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        apiStore.loadApiFormSpringBeans();
    }

}
