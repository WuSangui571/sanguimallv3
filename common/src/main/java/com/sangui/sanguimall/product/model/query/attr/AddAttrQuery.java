package com.sangui.sanguimall.product.model.query.attr;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-17
 * @Description:
 * @Version: 1.0
 */
@Data
public class AddAttrQuery {
    private Long id;
    private String attrName;
    private Byte searchType;
    private Byte valueType;
    private String valueSelect;
    private Byte attrType;
    private Long enable;
    private Long catelogId;
    private Byte showDesc;
    private String icon;
}
