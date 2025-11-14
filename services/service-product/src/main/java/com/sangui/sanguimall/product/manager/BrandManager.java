package com.sangui.sanguimall.product.manager;


import com.sangui.sanguimall.product.model.query.AddBrandQuery;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-14
 * @Description:
 * @Version: 1.0
 */
public interface BrandManager {
    int updateByPrimaryKeySelective(AddBrandQuery addBrandQuery);
}
