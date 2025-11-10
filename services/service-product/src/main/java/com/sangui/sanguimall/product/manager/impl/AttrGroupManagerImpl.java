package com.sangui.sanguimall.product.manager.impl;


import com.sangui.sanguimall.product.manager.AttrGroupManager;
import com.sangui.sanguimall.product.mapper.AttrAttrgroupRelationMapper;
import com.sangui.sanguimall.product.mapper.AttrGroupMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */
@Service
@Transactional
public class AttrGroupManagerImpl implements AttrGroupManager {

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Resource
    AttrAttrgroupRelationMapper attrAttrgroupRelationMapper;

//    @Override
//    public int delAttrGroupById(Long id) {
//
//        return 0;
//    }
//
//    @Override
//    public int batchDelAttrGroupByIds(String ids) {
//        return 0;
//    }
}
