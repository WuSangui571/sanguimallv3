package com.sangui.sanguimall.product.manager.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.manager.CateBrandRelationManager;
import com.sangui.sanguimall.product.mapper.BrandMapper;
import com.sangui.sanguimall.product.mapper.CategoryBrandRelationMapper;
import com.sangui.sanguimall.product.mapper.CategoryMapper;
import com.sangui.sanguimall.product.model.converter.CategoryBrandRelationConverter;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.query.CategoryBrandRelationQuery;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-13
 * @Description:
 * @Version: 1.0
 */
@Service
public class CateBrandRelationManagerImpl implements CateBrandRelationManager {
    @Resource
    CategoryBrandRelationConverter categoryBrandRelationConverter;
    @Resource
    CategoryBrandRelationMapper categoryBrandRelationMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    BrandMapper brandMapper;

    @Override
    public PageInfo<CategoryBrandRelationVo> getRelationsById(Integer current, Long brandId) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);

        // 2. 查询数据
        List<CategoryBrandRelationDo> doList = categoryBrandRelationMapper.selectRelationsByBrandId(brandId);

        // 3. 获取 PageInfo 对象，封装分页信息
        PageInfo<CategoryBrandRelationDo> pageInfoDo = new PageInfo<>(doList);

        // 4. 转换为 Vo 列表
        List<CategoryBrandRelationVo> voList = new ArrayList<>();
        for (CategoryBrandRelationDo categoryBrandRelationDo : doList) {
            CategoryBrandRelationVo categoryBrandRelationVo = categoryBrandRelationConverter.doToVo(categoryBrandRelationDo);

            // 设置路径
            String grandSonPath = categoryBrandRelationDo.getCatelogName();
            CategoryDo grandSonCategoryDo = categoryMapper.selectByPrimaryKey(categoryBrandRelationDo.getCatelogId());
            CategoryDo sonCategoryDo = categoryMapper.selectByPrimaryKey(grandSonCategoryDo.getParentCid());
            String sonPath=sonCategoryDo.getName();
            CategoryDo fatherCategoryDo = categoryMapper.selectByPrimaryKey(sonCategoryDo.getParentCid());
            String fatherPath=fatherCategoryDo.getName();
            categoryBrandRelationVo.setPath(fatherPath + "->" + sonPath + "->" + grandSonPath);

            voList.add(categoryBrandRelationVo);
        }

        // 5. 封装分页数据到 PageInfo
        PageInfo<CategoryBrandRelationVo> pageInfoVo = new PageInfo<>(voList);

        // 手动传递分页信息
        // 总记录数
        pageInfoVo.setTotal(pageInfoDo.getTotal());
        // 总页数
        pageInfoVo.setPages(pageInfoDo.getPages());
        // 当前页
        pageInfoVo.setPageNum(pageInfoDo.getPageNum());
        // 每页数据量
        pageInfoVo.setPageSize(pageInfoDo.getPageSize());
        // 起始行
        pageInfoVo.setStartRow(pageInfoDo.getStartRow());
        // 结束行
        pageInfoVo.setEndRow(pageInfoDo.getEndRow());
        // 当前页的数据条数
        pageInfoVo.setSize(pageInfoDo.getSize());

        // 6. 返回最终的分页数据
        return pageInfoVo;
    }

    @Override
    public int addRelation(CategoryBrandRelationQuery categoryBrandRelationQuery) {
        Long brandId = categoryBrandRelationQuery.getBrandId();
        Long categoryId = categoryBrandRelationQuery.getCategoryId();
        CategoryBrandRelationDo categoryBrandRelationDo = categoryBrandRelationMapper.selectByCategoryIdAndBrandId(brandId, categoryId);
        if (categoryBrandRelationDo != null) {
            throw new RuntimeException("已存在该分类");
        } else {
            categoryBrandRelationDo = new CategoryBrandRelationDo();
            CategoryDo categoryDo = categoryMapper.selectByPrimaryKey(categoryId);
            BrandDo brandDo = brandMapper.selectByPrimaryKey(brandId);
            categoryBrandRelationDo.setBrandId(brandId);
            categoryBrandRelationDo.setCatelogId(categoryId);
            categoryBrandRelationDo.setCatelogId(categoryId);
            categoryBrandRelationDo.setBrandName(brandDo.getName());
            categoryBrandRelationDo.setCatelogName(categoryDo.getName());
            return categoryBrandRelationMapper.insertSelective(categoryBrandRelationDo);
        }
    }
}
