package com.sangui.sanguimall.product.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 商品三级分类（Vo）
 * @Version: 1.0
 */
@Data
public class CategoryVo implements Serializable {
    /**
     * 分类id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String label;

    /**
     * 孩子分类
     */
    private List<CategoryVo> childList;
}
