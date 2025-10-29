package com.sangui.sanguimall.product.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 属性分组
 * pms_attr_group
 */
@Data
public class AttrGroupDo implements Serializable {
    /**
     * 分组id
     */
    private Long attrGroupId;

    /**
     * 组名
     */
    private String attrGroupName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String descript;

    /**
     * 组图标
     */
    private String icon;

    /**
     * 所属分类id
     */
    private Long catelogId;

    private static final long serialVersionUID = 1L;
}