package com.sangui.sanguimall.product.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.query.attr.AddAttrQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrEnableQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrSearchTypeQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrShowDescQuery;
import com.sangui.sanguimall.product.model.vo.AttrDetailVo;
import com.sangui.sanguimall.product.model.vo.AttrForAttrTypeOptionVo;
import com.sangui.sanguimall.product.model.vo.AttrVo;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
public interface AttrService {
    PageInfo<AttrVo> getAttrsByPage(Integer current);

    PageInfo<AttrVo> searchAttrsByKeyword(Integer current, String keyword);

    int editAttrSearchType(EditAttrSearchTypeQuery editAttrSearchTypeQuery);

    int editAttrShowDesc(EditAttrShowDescQuery editAttrShowDescQuery);

    int editAttrEnable(EditAttrEnableQuery editAttrEnableQuery);

    List<AttrForAttrTypeOptionVo> getAttrTypeOption();

    int addAttr(AddAttrQuery addAttrQuery);

    AttrDetailVo getAttrDetail(Long attrId);

    int editAttr(AddAttrQuery addAttrQuery);

    int delAttrById(Long id);

    int delAttrByIds(String ids);
}
