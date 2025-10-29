package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrGroupDo;

public interface AttrGroupMapper {
    int deleteByPrimaryKey(Long attrGroupId);

    int insert(AttrGroupDo record);

    int insertSelective(AttrGroupDo record);

    AttrGroupDo selectByPrimaryKey(Long attrGroupId);

    int updateByPrimaryKeySelective(AttrGroupDo record);

    int updateByPrimaryKey(AttrGroupDo record);
}