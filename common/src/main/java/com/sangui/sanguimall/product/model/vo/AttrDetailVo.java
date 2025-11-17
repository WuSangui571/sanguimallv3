package com.sangui.sanguimall.product.model.vo;

import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-19
 * @Description: 规格参数详情，包含分类路径
 * @Version: 1.0
 */
@Data
public class AttrDetailVo {
    private Long attrId;
    private String attrName;
    private Byte searchType;
    private Byte valueType;
    private String icon;
    private String valueSelect;
    private Byte attrType;
    private Long enable;
    private Long catelogId;
    private Byte showDesc;
    private Long oneCategoryId;
    private Long twoCategoryId;
    private Long threeCategoryId;
}
