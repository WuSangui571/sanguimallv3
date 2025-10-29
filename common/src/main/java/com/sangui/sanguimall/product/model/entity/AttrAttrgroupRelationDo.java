package com.sangui.sanguimall.product.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * 属性&属性分组关联
 * pms_attr_attrgroup_relation
 */
@Data
public class AttrAttrgroupRelationDo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 属性id
     */
    private Long attrId;

    /**
     * 属性分组id
     */
    private Long attrGroupId;

    /**
     * 属性组内排序
     */
    private Integer attrSort;

    private static final long serialVersionUID = 1L;
}