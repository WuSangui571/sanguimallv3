package com.sangui.sanguimall.product.service;

import com.sangui.sanguimall.product.model.vo.AttrVo;

import java.util.List;

/**
 * 属性与属性分组关联相关服务
 */
public interface AttrAttrgroupRelationService {

    /**
     * 查询指定三级分类下未被分组关联的规格参数列表
     */
    List<AttrVo> listUnrelatedAttrs(Long catelogId);

    /**
     * 建立属性与分组的关联
     */
    int addRelation(Long attrGroupId, Long attrId);

    /**
     * 查询指定分组下已关联的规格参数
     */
    List<AttrVo> listRelatedAttrs(Long attrGroupId);

    /**
     * 解除属性与分组的关联
     */
    int removeRelation(Long attrGroupId, Long attrId);
}
