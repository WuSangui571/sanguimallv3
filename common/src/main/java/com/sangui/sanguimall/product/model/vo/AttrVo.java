package com.sangui.sanguimall.product.model.vo;


import com.sangui.sanguimall.product.model.entity.CategoryDo;
import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttrVo {
    private Long attrId;
    private String attrName;
    private Integer searchType;
    private Integer valueType;
    private String icon;
    private String valueSelect;
    private Byte attrType;
    private Long enable;
    private Long catelogId;
    private Byte showDesc;
    private CategoryVo categoryVo;
}
