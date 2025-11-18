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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: CategoryServiceImpl（修改 category 的信息记得同步 attrGroup 等关联）
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

    private static final long TREE_CACHE_TTL_MS = 60_000L;
    private volatile List<CategoryVo> cachedTree;
    private volatile long treeCacheTime;

    @Override
    public List<CategoryVo> listWithTree() {
        List<CategoryVo> cache = cachedTree;
        long now = System.currentTimeMillis();
        if (cache != null && (now - treeCacheTime) < TREE_CACHE_TTL_MS) {
            return cache;
        }

        synchronized (this) {
            cache = cachedTree;
            now = System.currentTimeMillis();
            if (cache != null && (now - treeCacheTime) < TREE_CACHE_TTL_MS) {
                return cache;
            }

            List<CategoryDo> all = categoryMapper.selectAll();
            if (all == null || all.isEmpty()) {
                cachedTree = Collections.emptyList();
                treeCacheTime = now;
                return cachedTree;
            }

            List<CategoryDo> visibleList = all.stream()
                    .filter(c -> Objects.equals(c.getShowStatus(), (byte) 1))
                    .collect(Collectors.toList());

            if (visibleList.isEmpty()) {
                cachedTree = Collections.emptyList();
                treeCacheTime = now;
                return cachedTree;
            }

            Map<Long, CategoryVo> voMap = visibleList.stream()
                    .map(categoryConverter::doToVo)
                    .peek(vo -> vo.setChildren(new ArrayList<>()))
                    .collect(Collectors.toMap(CategoryVo::getId, v -> v));

            List<CategoryVo> rootList = new ArrayList<>();

            for (CategoryDo categoryDo : visibleList) {
                Long catId = categoryDo.getCatId();
                Long parentCid = categoryDo.getParentCid();
                Integer level = categoryDo.getCatLevel();

                CategoryVo currentVo = voMap.get(catId);
                if (currentVo == null) {
                    continue;
                }

                if (level != null && level == 1) {
                    rootList.add(currentVo);
                } else {
                    CategoryVo parentVo = voMap.get(parentCid);
                    if (parentVo != null) {
                        if (parentVo.getChildren() == null) {
                            parentVo.setChildren(new ArrayList<>());
                        }
                        parentVo.getChildren().add(currentVo);
                    }
                }
            }

            cachedTree = rootList;
            treeCacheTime = now;
            return cachedTree;
        }
    }

    @Override
    public CategoryVo addCategory(CategoryQuery categoryQuery) {
        CategoryDo parentCategoryDo = categoryMapper.selectByPrimaryKey(categoryQuery.getParentId());
        CategoryDo newNodeCategoryDo = new CategoryDo();
        newNodeCategoryDo.setName(categoryQuery.getNewNodeLabel());
        newNodeCategoryDo.setParentCid(parentCategoryDo.getCatId());
        newNodeCategoryDo.setCatLevel(parentCategoryDo.getCatLevel() + 1);
        newNodeCategoryDo.setShowStatus((byte) 1);

        List<CategoryDo> sameParentCategoryDoList = categoryMapper.selectBySameParent(categoryQuery.getParentId());
        if (sameParentCategoryDoList.isEmpty()) {
            newNodeCategoryDo.setSort(0);
        } else {
            Integer maxSort = sameParentCategoryDoList.get(0).getSort();
            for (CategoryDo categoryDo : sameParentCategoryDoList) {
                if (categoryDo.getSort() > maxSort) {
                    maxSort = categoryDo.getSort();
                }
            }
            newNodeCategoryDo.setSort(maxSort + 1);
        }

        newNodeCategoryDo.setProductCount(0);
        categoryMapper.insert(newNodeCategoryDo);
        invalidateTreeCache();
        return categoryConverter.doToVo(categoryMapper.selectByPrimaryKey(newNodeCategoryDo.getCatId()));
    }

    @Override
    public int delCategoryByCatId(Long catId) {
        int count = categoryMapper.deleteByPrimaryKey(catId);
        if (count > 0) {
            invalidateTreeCache();
        }
        return count;
    }

    @Override
    public int editCategory(CategoryQuery categoryQuery) {
        int count = categoryManager.editCategory(categoryQuery);
        if (count > 0) {
            invalidateTreeCache();
        }
        return count;

    }

    @Override
    @Transactional
    public int editCategorySequence(CategorySequenceQuery categorySequenceQuery) {
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
        int count = categoryMapper.updateByPrimaryKeySelective(categoryNeedSequenceDo);
        if (count > 0) {
            invalidateTreeCache();
        }
        return count;
    }

    @Override
    public List<OneTwoThreeCategoryVo> getOne() {
        List<CategoryDo> categoryDoList = categoryMapper.selectOne();
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo : categoryDoList) {
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }

    @Override
    public List<OneTwoThreeCategoryVo> getTwo(Long oneOptionsId) {
        List<CategoryDo> categoryDoList = categoryMapper.selectTwo(oneOptionsId);
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo : categoryDoList) {
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }

    @Override
    public List<OneTwoThreeCategoryVo> getThree(Long twoOptionsId) {
        List<CategoryDo> categoryDoList = categoryMapper.selectThree(twoOptionsId);
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = new ArrayList<>();
        for (CategoryDo categoryDo : categoryDoList) {
            oneTwoThreeCategoryVoList.add(categoryConverter.doToVo2(categoryDo));
        }
        return oneTwoThreeCategoryVoList;
    }

    @Override
    public String getCategoryPath(Long id) {
        CategoryDo grandSonCategoryDo = categoryMapper.selectByPrimaryKey(id);
        Long sonCategoryId = grandSonCategoryDo.getParentCid();
        if (sonCategoryId == 0) {
            throw new RuntimeException("不能直接查询一级分类的路径！");
        }
        CategoryDo sonCategoryDo = categoryMapper.selectByPrimaryKey(sonCategoryId);
        Long fatherCategoryId = sonCategoryDo.getParentCid();
        CategoryDo fatherCategoryDo = categoryMapper.selectByPrimaryKey(fatherCategoryId);
        String path = fatherCategoryDo.getName() + ";" + sonCategoryDo.getName();
        return path + ";" + grandSonCategoryDo.getName();
    }

    private void invalidateTreeCache() {
        cachedTree = null;
        treeCacheTime = 0L;
    }
}
