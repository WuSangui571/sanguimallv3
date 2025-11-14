package com.sangui.sanguimall.product.service.impl;


import com.sangui.sanguimall.product.manager.CategoryManager;
import com.sangui.sanguimall.product.mapper.CategoryMapper;
import com.sangui.sanguimall.product.model.converter.CategoryConverter;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.query.CategoryQuery;
import com.sangui.sanguimall.product.model.query.CategorySequenceQuery;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.model.vo.OneTwoThreeCategoryVo;
import com.sangui.sanguimall.product.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryServiceImpl(修改 category 的信息记得修改 attrGroup 表)
 * @Version: 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    @Resource
    private CategoryConverter categoryConverter;

    @Resource
    private CategoryManager categoryManager;

    @Override
    public List<CategoryVo> listWithTree() {
        List<CategoryDo> categoryDoList = categoryMapper.selectAll();
        List<CategoryVo> result = new ArrayList<>();
        for (CategoryDo categoryDo : categoryDoList) {
            if (categoryDo.getShowStatus() == 1) {
                CategoryVo categoryVo = categoryConverter.doToVo(categoryDo);
                if (categoryDo.getCatLevel() == 1) {
                    categoryVo.setChildren(new ArrayList<>());
                    result.add(categoryVo);
                    continue;
                }
                if (categoryDo.getCatLevel() == 2) {
                    categoryVo.setChildren(new ArrayList<>());
                    for (CategoryVo item : result) {
                        if (item.getId().equals(categoryDo.getParentCid())) {
                            List<CategoryVo> tempList = item.getChildren();
                            tempList.add(categoryVo);
                            item.setChildren(tempList);
                        }
                    }
                    continue;
                }
                if (categoryDo.getCatLevel() == 3) {
                    categoryVo.setChildren(null);
                    for (CategoryVo item : result) {
                        for (CategoryVo item2 : item.getChildren()) {
                            if (item2.getId().equals(categoryDo.getParentCid())) {
                                List<CategoryVo> tempList = item2.getChildren();
                                tempList.add(categoryVo);
                                item2.setChildren(tempList);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public CategoryVo addCategory(CategoryQuery categoryQuery) {
        //System.out.println(categoryQuery);
        CategoryDo parentCategoryDo = categoryMapper.selectByPrimaryKey(categoryQuery.getParentId());
        CategoryDo newNodeCategoryDo = new CategoryDo();
        newNodeCategoryDo.setName(categoryQuery.getNewNodeLabel());
        newNodeCategoryDo.setParentCid(parentCategoryDo.getCatId());
        newNodeCategoryDo.setCatLevel(parentCategoryDo.getCatLevel() + 1);
        newNodeCategoryDo.setShowStatus((byte) 1);

        // 设置排序，设为最大的排序 + 1
        List<CategoryDo> sameParentCategoryDoList = categoryMapper.selectBySameParent(categoryQuery.getParentId());
        if (sameParentCategoryDoList.isEmpty()){
            newNodeCategoryDo.setSort(0);
        }else {
            Integer maxSort = sameParentCategoryDoList.get(0).getSort();
            for (CategoryDo categoryDo:sameParentCategoryDoList){
                if (categoryDo.getSort() > maxSort) {
                    maxSort = categoryDo.getSort();
                }
            }
            newNodeCategoryDo.setSort(maxSort + 1);
        }

        newNodeCategoryDo.setProductCount(0);
        categoryMapper.insert(newNodeCategoryDo);
        //System.out.println(newNodeCategoryDo);
        return categoryConverter.doToVo(categoryMapper.selectByPrimaryKey(newNodeCategoryDo.getCatId()));
    }

    @Override
    public int delCategoryByCatId(Long catId) {
        return categoryMapper.deleteByPrimaryKey(catId);
    }

    @Override
    public int editCategory(CategoryQuery categoryQuery) {
        return categoryManager.editCategory(categoryQuery);

    }

    @Override
    @Transactional
    public int editCategorySequence(CategorySequenceQuery categorySequenceQuery) {
        //System.out.println(categorySequenceQuery);
        CategoryDo categoryNeedSequenceDo = categoryMapper.selectByPrimaryKey(categorySequenceQuery.getDraggingNodeId());
        CategoryDo sequenceNeedAfterMeDo = categoryMapper.selectByPrimaryKey(categorySequenceQuery.getAfterDropNodeId());
        List<CategoryDo> sameParentCategoryDoList = categoryMapper.selectBySameParent(categoryNeedSequenceDo.getParentCid());
        for (CategoryDo categoryDo : sameParentCategoryDoList) {
            if ((categoryDo.getSort() == sequenceNeedAfterMeDo.getSort() && categoryDo.getCatId() > sequenceNeedAfterMeDo.getCatId())
                    || (categoryDo.getSort() > sequenceNeedAfterMeDo.getSort())) {
                categoryDo.setSort(categoryDo.getSort() + 2);
                categoryMapper.updateByPrimaryKeySelective(categoryDo);
            }
        }
        categoryNeedSequenceDo.setSort(sequenceNeedAfterMeDo.getSort() + 1);
        return categoryMapper.updateByPrimaryKeySelective(categoryNeedSequenceDo);
    }

    @Override
    public List<OneTwoThreeCategoryVo> getOne() {
        List<CategoryDo> categoryDoList = categoryMapper.selectOne();
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo: categoryDoList){
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }

    @Override
    public List<OneTwoThreeCategoryVo> getTwo(Long oneOptionsId) {
        List<CategoryDo> categoryDoList = categoryMapper.selectTwo(oneOptionsId);
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo: categoryDoList){
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }

    @Override
    public List<OneTwoThreeCategoryVo> getThree(Long twoOptionsId) {
        List<CategoryDo> categoryDoList = categoryMapper.selectThree(twoOptionsId);
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo: categoryDoList){
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }
}
