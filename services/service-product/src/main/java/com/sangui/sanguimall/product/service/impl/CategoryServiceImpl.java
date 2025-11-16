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

import java.util.*;
import java.util.stream.Collectors;

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

//    @Override
//    public List<CategoryVo> listWithTree() {
//        List<CategoryDo> categoryDoList = categoryMapper.selectAll();
//        List<CategoryVo> result = new ArrayList<>();
//        for (CategoryDo categoryDo : categoryDoList) {
//            if (categoryDo.getShowStatus() == 1) {
//                CategoryVo categoryVo = categoryConverter.doToVo(categoryDo);
//                if (categoryDo.getCatLevel() == 1) {
//                    categoryVo.setChildren(new ArrayList<>());
//                    result.add(categoryVo);
//                    continue;
//                }
//                if (categoryDo.getCatLevel() == 2) {
//                    categoryVo.setChildren(new ArrayList<>());
//                    for (CategoryVo item : result) {
//                        if (item.getId().equals(categoryDo.getParentCid())) {
//                            List<CategoryVo> tempList = item.getChildren();
//                            tempList.add(categoryVo);
//                            item.setChildren(tempList);
//                        }
//                    }
//                    continue;
//                }
//                if (categoryDo.getCatLevel() == 3) {
//                    categoryVo.setChildren(null);
//                    for (CategoryVo item : result) {
//                        for (CategoryVo item2 : item.getChildren()) {
//                            if (item2.getId().equals(categoryDo.getParentCid())) {
//                                List<CategoryVo> tempList = item2.getChildren();
//                                tempList.add(categoryVo);
//                                item2.setChildren(tempList);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return result;
//    }

    @Override
    public List<CategoryVo> listWithTree() {

        // 1. 一次性查出所有分类（建议 SQL 里就按 sort 排好序）
        List<CategoryDo> all = categoryMapper.selectAll();
        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 过滤出 showStatus = 1 的
        List<CategoryDo> visibleList = all.stream()
                .filter(c -> Objects.equals(c.getShowStatus(), (byte) 1))
                .collect(Collectors.toList());

        if (visibleList.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. DO 全部转成 VO，放到 Map 里，方便 O(1) 找父节点
        Map<Long, CategoryVo> voMap = visibleList.stream()
                .map(categoryConverter::doToVo)
                .peek(vo -> vo.setChildren(new ArrayList<>())) // 先给个空列表，方便后面 add
                .collect(Collectors.toMap(CategoryVo::getId, v -> v));

        List<CategoryVo> rootList = new ArrayList<>();

        // 4. 组装父子关系
        for (CategoryDo categoryDo : visibleList) {
            Long catId = categoryDo.getCatId();
            Long parentCid = categoryDo.getParentCid();
            Integer level = categoryDo.getCatLevel();

            CategoryVo currentVo = voMap.get(catId);
            if (currentVo == null) {
                // 理论上不会发生，防一手 NPE
                continue;
            }

            if (level != null && level == 1) {
                // 一级分类，直接加入根节点列表
                rootList.add(currentVo);
            } else {
                // 非一级分类，挂到父节点下面
                CategoryVo parentVo = voMap.get(parentCid);
                if (parentVo != null) {
                    if (parentVo.getChildren() == null) {
                        parentVo.setChildren(new ArrayList<>());
                    }
                    parentVo.getChildren().add(currentVo);
                } else {
                    // 父节点不存在，一般是数据问题，这里简单略过
                    // 可以打个日志方便排查
                    // log.warn("Category parent not found, catId={}, parentCid={}", catId, parentCid);
                }
            }
        }

        // 5. 如果你希望按照 sort 排序，可以在 SQL 里 order by sort
        //    或者在这里再做一次递归排序（前提是需要把 sort 加到 CategoryVo 里）

        return rootList;
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

    @Override
    public String getCategoryPath(Long id) {
//        System.out.println("查询的 id =" + id);
        CategoryDo grandSonCategoryDo = categoryMapper.selectByPrimaryKey(id);
//        System.out.println("查询的 grandSonCategoryDo = " + grandSonCategoryDo);
        Long sonCategoryId = grandSonCategoryDo.getParentCid();
        if (sonCategoryId == 0){
            throw new RuntimeException("不能直接查询一级分类的路径！");
        }
        CategoryDo sonCategoryDo = categoryMapper.selectByPrimaryKey(sonCategoryId);
        Long fatherCategoryId = sonCategoryDo.getParentCid();
        if (fatherCategoryId == 0){
            throw new RuntimeException("不能直接查询二级分类的路径！");
        }
        CategoryDo fatherCategoryDo = categoryMapper.selectByPrimaryKey(fatherCategoryId);
        return fatherCategoryDo.getName() + ";" + sonCategoryDo.getName() + ";" + grandSonCategoryDo.getName();
    }
}
