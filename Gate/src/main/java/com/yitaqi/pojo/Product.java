package com.yitaqi.pojo;

import java.math.BigDecimal;

/**
 * @author xue
 */
public class Product {

    private BigDecimal id;
    private String pdname;
    private BigDecimal price;
    // 验证接口规范
//    private Object property;
//
//    public Object getProperty() {
//        return property;
//    }
//
//    public void setProperty(Object property) {
//        this.property = property;
//    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getPdname() {
        return pdname;
    }

    public void setPdname(String pdname) {
        this.pdname = pdname;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
