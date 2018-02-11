package com.yitaqi.dao;

import com.yitaqi.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @author xue
 */
@Mapper
public interface ProductMapper {

    /**
     * 根据id 获取产品
     * @param id
     * @return product
     */
    @Select("SELECT ID,PDNAME,PRICE FROM T_PRODUCT WHERE ID = #{id}")
    Product getProduct(BigDecimal id);
}
