package com.sangui.sanguimall.product.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.query.BrandEditShowStatusQuery;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-07
 * @Description:
 * @Version: 1.0
 */
public interface BrandService {
    PageInfo<BrandDo> getBrandsByPage(Integer current);

    int editBrandShowStatus(BrandEditShowStatusQuery brandEditShowStatusQuery);
}
