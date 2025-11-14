package com.sangui.sanguimall.product.manager.impl;


import com.sangui.sanguimall.product.manager.BrandManager;
import com.sangui.sanguimall.product.mapper.BrandMapper;
import com.sangui.sanguimall.product.mapper.CategoryBrandRelationMapper;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.entity.CategoryBrandRelationDo;
import com.sangui.sanguimall.product.model.query.AddBrandQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-14
 * @Description:
 * @Version: 1.0
 */
@Service
@Transactional
public class BrandManagerImpl implements BrandManager {
    @Resource
    BrandMapper brandMapper;

    @Resource
    CategoryBrandRelationMapper categoryBrandRelationMapper;

    @Override
    public int updateByPrimaryKeySelective(AddBrandQuery addBrandQuery) {
        BrandDo brandDo = new BrandDo();
        brandDo.setBrandId(addBrandQuery.getId());
        brandDo.setName(addBrandQuery.getName());
        brandDo.setLogo(addBrandQuery.getLogo());
        brandDo.setDescript(addBrandQuery.getDescript());
        brandDo.setShowStatus(addBrandQuery.getShowStatus());
        brandDo.setFirstLetter(addBrandQuery.getFirstLetter());
        brandDo.setSort(addBrandQuery.getSort());

        List<CategoryBrandRelationDo> categoryBrandRelationDoList = categoryBrandRelationMapper.selectRelationsByBrandId(addBrandQuery.getId());
        if (!categoryBrandRelationDoList.isEmpty()){
            for (CategoryBrandRelationDo categoryBrandRelationDo: categoryBrandRelationDoList){
                categoryBrandRelationDo.setBrandName(addBrandQuery.getName());
                categoryBrandRelationMapper.updateByPrimaryKeySelective(categoryBrandRelationDo);
            }
        }

        return brandMapper.updateByPrimaryKeySelective(brandDo);
    }
}
