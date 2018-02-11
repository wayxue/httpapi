package com.yitaqi.service;

import com.yitaqi.pojo.Product;

import java.math.BigDecimal;

/**
 * 用户服务接口
 * @author xue
 */
public interface ProductService {

    /**
     * 根据id 获取用户信息
     * @param id
     * @return Product
     */
    Product getProduct(BigDecimal id);
}
