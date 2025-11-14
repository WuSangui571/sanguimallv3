package com.sangui.sanguimall.product.manager;


import com.sangui.sanguimall.product.model.query.CategoryQuery;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-14
 * @Description:
 * @Version: 1.0
 */
public interface CategoryManager {
    int editCategory(CategoryQuery categoryQuery);
}
