package com.yitaqi.service;

import com.yitaqi.core.APIMapping;
import com.yitaqi.dao.ProductMapper;
import com.yitaqi.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 用户服务测试类
 * @author xue
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    @APIMapping("getProduct")
    public Product getProduct(BigDecimal id) {

        Product product = productMapper.getProduct(id);
        return product;
    }

    /**
     * http://localhost:8080/ser?method=exchange&params=%7Bid:1,product:%7Bpdname:%22998%22,price:234,id:21%7D%7D
     * @param id
     * @param product
     * @return
     */
    @APIMapping("exchange")
    public Product exchange(BigDecimal id, Product product) {

        Product product1 = new Product();
        product1.setPrice(new BigDecimal(123));
        product1.setPdname(product.getPdname());
        product1.setId(id);
        return product1;
    }
}
