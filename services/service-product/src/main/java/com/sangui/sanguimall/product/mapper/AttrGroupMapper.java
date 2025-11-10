package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrGroupDo;

import java.util.List;

/**
 * @author sangui
 */
public interface AttrGroupMapper {
    int deleteByPrimaryKey(Long attrGroupId);

    int insert(AttrGroupDo record);

    int insertSelective(AttrGroupDo record);

    AttrGroupDo selectByPrimaryKey(Long attrGroupId);

    int updateByPrimaryKeySelective(AttrGroupDo record);

    int updateByPrimaryKey(AttrGroupDo record);

    List<AttrGroupDo> selectAttrGroupsByPage(Long catelogId);

    int deleteByIds(String ids);
}