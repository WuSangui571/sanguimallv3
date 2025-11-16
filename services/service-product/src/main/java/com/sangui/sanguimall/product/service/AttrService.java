package com.sangui.sanguimall.product.service;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.query.attr.EditAttrEnableQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrSearchTypeQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrShowDescQuery;
import com.sangui.sanguimall.product.model.vo.AttrVo;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
public interface AttrService {
    PageInfo<AttrVo> getAttrsByPage(Integer current);

    int editAttrSearchType(EditAttrSearchTypeQuery editAttrSearchTypeQuery);

    int editAttrShowDesc(EditAttrShowDescQuery editAttrShowDescQuery);

    int editAttrEnable(EditAttrEnableQuery editAttrEnableQuery);
}
