package com.sangui.sanguimall.product.service;


import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryService
 * @Version: 1.0
 */
public interface CategoryService {
    List<CategoryVo> listWithTree();
}
