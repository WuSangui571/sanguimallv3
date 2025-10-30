package com.sangui.sanguimall.product.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-30
 * @Description:  前端返回的 CategoryQuery
 * @Version: 1.0
 */
@Data
public class CategoryQuery {
    private Long catId;
    private Long parentId;
    private String newNodeLabel;
}
