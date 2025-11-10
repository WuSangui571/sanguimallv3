package com.sangui.sanguimall.product.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttrGroupQuery {
    private Long id;
    private String attrGroupName;
    private Integer sort;
    private String descript;
    private String icon;
    private Long catelogId;

}
