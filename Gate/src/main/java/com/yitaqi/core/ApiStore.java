package com.yitaqi.core;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * API IOC 大仓库
 * @author xue
 */
public class ApiStore {

    private ApplicationContext applicationContext;
    // api 接口存储的地方
    private HashMap<String, ApiRunnable> apiMap = new HashMap<>();

    /**
     * spring ioc
     * @param applicationContext
     */
    public ApiStore(ApplicationContext applicationContext) {

        Assert.notNull(applicationContext, "applicationContext is null!");
        this.applicationContext = applicationContext;
    }

    /**
     * 基于spring ioc 容器中的 bean 查找对应的 api 方法
     */
    public void loadApiFormSpringBeans() {

        String[] names = applicationContext.getBeanDefinitionNames();
        Class<?> type;
        for (String name : names) {
            type = applicationContext.getType(name);
            for (Method method : type.getDeclaredMethods()) {
                APIMapping apiMapping = method.getAnnotation(APIMapping.class);
                if (apiMapping != null) {
                    // find target method
                    addApiItem(apiMapping, name, method);
                }
            }
        }
    }

    /**
     * 添加api
     * @param apiMapping
     * @param beanName
     * @param method
     */
    private void addApiItem(APIMapping apiMapping, String beanName, Method method) {

        // 验证接口模型规范
        for (Field field : method.getReturnType().getDeclaredFields()) {
            if (field.getType().equals(Object.class)) {
                throw new RuntimeException(String.format("%s,%s 不符合接口规范",
                        method.getDeclaringClass(), method.getName()));
            }
        }
        // 执行器
        ApiRunnable apiRunnable = new ApiRunnable();
        apiRunnable.apiName = apiMapping.value();
        apiRunnable.targetMethod = method;
        apiRunnable.targetName = beanName;
        apiMap.put(apiMapping.value(), apiRunnable);
    }

    public ApiRunnable findApiRunnable(String apiName, String version) {

//        return (ApiRunnable) apiMap.get(apiName + "_" + version);
        return apiMap.get(apiName);
    }

    public List<ApiRunnable> findApiRunnables(String apiName) {

        if (StringUtils.isEmpty(apiName)) {
            throw new IllegalArgumentException("apiName do not need null");
        }
        List<ApiRunnable> list = new ArrayList<>(20);
        for (ApiRunnable apiRunnable : apiMap.values()) {
            if (apiRunnable.apiName.equals(apiName)) {
                list.add(apiRunnable);
            }
        }
        return list;
    }

    public class ApiRunnable {

        String apiName;
        String targetName;
        Object target;
        Method targetMethod;

        public Object run(Object... args) throws InvocationTargetException, IllegalAccessException {

            if (target == null) {
                target = applicationContext.getBean(targetName);
            }
            return targetMethod.invoke(target, args);
        }
    }

}
