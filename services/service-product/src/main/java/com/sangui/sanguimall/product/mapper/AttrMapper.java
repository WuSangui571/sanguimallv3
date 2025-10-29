package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrDo;

public interface AttrMapper {
    int deleteByPrimaryKey(Long attrId);

    int insert(AttrDo record);

    int insertSelective(AttrDo record);

    AttrDo selectByPrimaryKey(Long attrId);

    int updateByPrimaryKeySelective(AttrDo record);

    int updateByPrimaryKey(AttrDo record);
}