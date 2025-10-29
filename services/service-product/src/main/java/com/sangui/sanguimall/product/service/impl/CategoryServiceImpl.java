package com.sangui.sanguimall.product.service.impl;


import com.sangui.sanguimall.product.mapper.CategoryMapper;
import com.sangui.sanguimall.product.model.converter.CategoryConverter;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryServiceImpl
 * @Version: 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    @Resource
    private CategoryConverter categoryConverter;

    @Override
    public List<CategoryVo> listWithTree() {
        List<CategoryDo> categoryDoList = categoryMapper.selectAll();
        List<CategoryVo> result = new ArrayList<>();
        for (CategoryDo categoryDo: categoryDoList){
            if (categoryDo.getShowStatus() == 1){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(categoryDo,categoryVo);
                CategoryVo categoryVo = categoryConverter.doToVo(categoryDo);
                if (categoryDo.getCatLevel() == 1){
                    categoryVo.setChildList(new ArrayList<>());
                    result.add(categoryVo);
                    continue;
                }
                if (categoryDo.getCatLevel() == 2){
                    categoryVo.setChildList(new ArrayList<>());
                    for (CategoryVo item:result){
                        if (item.getCatId().equals(categoryDo.getParentCid())){
                            List<CategoryVo> tempList = item.getChildList();
                            tempList.add(categoryVo);
                            item.setChildList(tempList);
                        }
                    }
                    continue;
                }
                if (categoryDo.getCatLevel() == 3){
                    categoryVo.setChildList(null);
                    for (CategoryVo item:result){
                        for (CategoryVo item2:item.getChildList()){
                            if (item2.getCatId().equals(categoryDo.getParentCid())){
                                List<CategoryVo> tempList = item2.getChildList();
                                tempList.add(categoryVo);
                                item2.setChildList(tempList);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
