package com.sangui.sanguimall.product.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * sku销售属性&值
 * pms_sku_sale_attr_value
 */
@Data
public class SkuSaleAttrValueDo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * attr_id
     */
    private Long attrId;

    /**
     * 销售属性名
     */
    private String attrName;

    /**
     * 销售属性值
     */
    private String attrValue;

    /**
     * 顺序
     */
    private Integer attrSort;

    private static final long serialVersionUID = 1L;
}