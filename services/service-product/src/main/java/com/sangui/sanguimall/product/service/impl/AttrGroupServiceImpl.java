package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.manager.AttrGroupManager;
import com.sangui.sanguimall.product.mapper.AttrGroupMapper;
import com.sangui.sanguimall.product.model.converter.AttrGroupConverter;
import com.sangui.sanguimall.product.model.entity.AttrGroupDo;
import com.sangui.sanguimall.product.model.query.AttrGroupQuery;
import com.sangui.sanguimall.product.model.vo.AttrGroupVo;
import com.sangui.sanguimall.product.service.AttrGroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */
@Service
public class AttrGroupServiceImpl implements AttrGroupService {
    @Resource
    AttrGroupMapper attrGroupMapper;

    @Resource
    AttrGroupConverter attrGroupConverter;

    @Resource
    AttrGroupManager attrGroupManager;

    @Override
    public PageInfo<AttrGroupVo> getAttrGroupsByPage(Integer current, Long catelogId) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2. 查询
        List<AttrGroupDo> list = attrGroupMapper.selectAttrGroupsByPage(catelogId);

        List<AttrGroupVo> res = new ArrayList<>();
        // 3. 封装分页数据到 PageInfo
        for (AttrGroupDo attrGroupDo:list){
            res.add(attrGroupConverter.doToVo(attrGroupDo));
        }
        return new PageInfo<>(res);
    }

    @Override
    public int addAttrGroup(AttrGroupQuery attrGroupQuery) {
        //System.out.println("后端接收到的 attrGroupQuery = "+ attrGroupQuery);
        AttrGroupDo attrGroupDo = new AttrGroupDo();
        attrGroupDo.setAttrGroupName(attrGroupQuery.getAttrGroupName());
        attrGroupDo.setSort(attrGroupQuery.getSort());
        attrGroupDo.setDescript(attrGroupQuery.getDescript());
        attrGroupDo.setIcon(attrGroupQuery.getIcon());
        attrGroupDo.setCatelogId(attrGroupQuery.getCatelogId());
        return attrGroupMapper.insertSelective(attrGroupDo);
    }

    @Override
    public AttrGroupVo getSingleAttrGroupById(Long id) {
        AttrGroupDo attrGroupDo = attrGroupMapper.selectByPrimaryKey(id);

        return attrGroupConverter.doToVo(attrGroupDo);
    }

    @Override
    public int editAttrGroup(AttrGroupQuery attrGroupQuery) {
        AttrGroupDo attrGroupDo = attrGroupMapper.selectByPrimaryKey(attrGroupQuery.getId());
        attrGroupDo.setAttrGroupName(attrGroupQuery.getAttrGroupName());
        attrGroupDo.setSort(attrGroupQuery.getSort());
        attrGroupDo.setDescript(attrGroupQuery.getDescript());
        attrGroupDo.setIcon(attrGroupQuery.getIcon());
        return attrGroupMapper.updateByPrimaryKeySelective(attrGroupDo);
    }

    @Override
    public int delAttrGroupById(Long id) {
        return attrGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelAttrGroupByIds(String ids) {
        return attrGroupMapper.deleteByIds(ids);
    }
}
