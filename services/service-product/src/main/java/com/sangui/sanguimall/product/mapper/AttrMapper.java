package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.AttrDo;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AttrMapper {
    int deleteByPrimaryKey(Long attrId);

    int deleteByIds(String ids);

    int insert(AttrDo record);

    int insertSelective(AttrDo record);

    AttrDo selectByPrimaryKey(Long attrId);

    int updateByPrimaryKeySelective(AttrDo record);

    int updateByPrimaryKey(AttrDo record);

    List<AttrDo> selectAttrsByPage();

    List<AttrDo> selectAttrsByKeyword(@Param("keyword") String keyword,
                                      @Param("matchSingleValue") Boolean matchSingleValue,
                                      @Param("matchMultiValue") Boolean matchMultiValue,
                                      @Param("matchHybridType") Boolean matchHybridType,
                                      @Param("matchSaleType") Boolean matchSaleType,
                                      @Param("matchBaseType") Boolean matchBaseType);

    List<AttrDo> selectUnrelatedAttrsByCatelogId(Long catelogId);

    List<AttrDo> selectRelatedAttrsByGroupId(Long attrGroupId);
}
