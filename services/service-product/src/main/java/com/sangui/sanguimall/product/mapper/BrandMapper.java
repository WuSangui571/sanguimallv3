package com.sangui.sanguimall.product.mapper;

import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandMapper {
    int deleteByPrimaryKey(Long brandId);

    int insert(BrandDo record);

    int insertSelective(BrandDo record);

    BrandDo selectByPrimaryKey(Long brandId);

    int updateByPrimaryKeySelective(BrandDo record);

    int updateByPrimaryKey(BrandDo record);


    List<BrandDo> selectBrandByPage();

    int deleteByIds(String ids);

    List<BrandDo> selectBrandByIds(String ids);

    List<BrandDo> selectBrandBySelect(@Param("selectValue")String selectValue);
}