package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrAttrgroupRelationDo;

public interface AttrAttrgroupRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AttrAttrgroupRelationDo record);

    int insertSelective(AttrAttrgroupRelationDo record);

    AttrAttrgroupRelationDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AttrAttrgroupRelationDo record);

    int updateByPrimaryKey(AttrAttrgroupRelationDo record);
}