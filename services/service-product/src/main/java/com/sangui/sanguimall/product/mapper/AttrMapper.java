package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrDo;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;

import java.util.List;

public interface AttrMapper {
    int deleteByPrimaryKey(Long attrId);

    int deleteByIds(String ids);

    int insert(AttrDo record);

    int insertSelective(AttrDo record);

    AttrDo selectByPrimaryKey(Long attrId);

    int updateByPrimaryKeySelective(AttrDo record);

    int updateByPrimaryKey(AttrDo record);

    List<AttrDo> selectAttrsByPage();
}
