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
     * 对应 CategoryDO 里的 catId 属性，即分类 id
     */
    private Long id;

    /**
     * 对应 CategoryDO 里的 name 属性，即分类名称
     */
    private String label;

    /**
     * 对应 CategoryDO 里的 catLevel 属性，即层级
     */
    private Integer level;

    /**
     * 无 CategoryDO 里的对应
     */
    private List<CategoryVo> children;
}
