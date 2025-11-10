package com.sangui.sanguimall.product.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.AttrGroupDo;
import com.sangui.sanguimall.product.model.query.AttrGroupQuery;
import com.sangui.sanguimall.product.model.vo.AttrGroupVo;
import org.springframework.stereotype.Service;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */

public interface AttrGroupService {
    PageInfo<AttrGroupVo> getAttrGroupsByPage(Integer current, Long catelogId);

    int addAttrGroup(AttrGroupQuery attrGroupQuery);

    AttrGroupVo getSingleAttrGroupById(Long id);

    int editAttrGroup(AttrGroupQuery attrGroupQuery);

    int delAttrGroupById(Long id);

    int batchDelAttrGroupByIds(String ids);
}
