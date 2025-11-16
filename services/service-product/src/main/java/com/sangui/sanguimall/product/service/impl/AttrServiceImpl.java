package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.mapper.AttrMapper;
import com.sangui.sanguimall.product.model.converter.AttrConverter;
import com.sangui.sanguimall.product.model.entity.AttrDo;
import com.sangui.sanguimall.product.model.query.attr.EditAttrEnableQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrSearchTypeQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrShowDescQuery;
import com.sangui.sanguimall.product.model.vo.AttrVo;
import com.sangui.sanguimall.product.service.AttrService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
@Service
public class AttrServiceImpl implements AttrService {
    @Resource
    AttrConverter attrConverter;

    @Resource
    AttrMapper attrMapper;

    @Override
    public PageInfo<AttrVo> getAttrsByPage(Integer current) {
        // 1. 设置 PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);

        // 2. 查询数据
        List<AttrDo> doList = attrMapper.selectAttrsByPage();

        // 3. 获取 PageInfo 对象，封装分页信息
        PageInfo<AttrDo> pageInfoDo = new PageInfo<>(doList);

        // 4. 转换为 Vo 列表
        List<AttrVo> voList = new ArrayList<>();
        //System.out.println("myDoList=" + doList);
        for (AttrDo attrDo : doList) {
            voList.add(attrConverter.doToVo(attrDo));
        }
        //System.out.println("myVoList=" + voList);

        // 5. 封装分页数据到 PageInfo
        PageInfo<AttrVo> pageInfoVo = new PageInfo<>(voList);

        // 手动传递分页信息
        // 总记录数
        pageInfoVo.setTotal(pageInfoDo.getTotal());
        // 总页数
        pageInfoVo.setPages(pageInfoDo.getPages());
        // 当前页
        pageInfoVo.setPageNum(pageInfoDo.getPageNum());
        // 每页数据量
        pageInfoVo.setPageSize(pageInfoDo.getPageSize());
        // 起始行
        pageInfoVo.setStartRow(pageInfoDo.getStartRow());
        // 结束行
        pageInfoVo.setEndRow(pageInfoDo.getEndRow());
        // 当前页的数据条数
        pageInfoVo.setSize(pageInfoDo.getSize());

        // 6. 返回最终的分页数据
        return pageInfoVo;
    }

    @Override
    public int editAttrSearchType(EditAttrSearchTypeQuery editAttrSearchTypeQuery) {
        //System.out.println(editAttrSearchTypeQuery);
        Long attrId = editAttrSearchTypeQuery.getAttrId();
        Boolean flag = editAttrSearchTypeQuery.getFlag();
        AttrDo attrDo = attrMapper.selectByPrimaryKey(attrId);
        if (flag){
            attrDo.setSearchType((byte) 1);
        }else {
            attrDo.setSearchType((byte) 0);
        }
        return attrMapper.updateByPrimaryKeySelective(attrDo);
    }

    @Override
    public int editAttrShowDesc(EditAttrShowDescQuery editAttrShowDescQuery) {
        //System.out.println(editAttrShowDescQuery);
        Long attrId = editAttrShowDescQuery.getAttrId();
        Boolean flag = editAttrShowDescQuery.getFlag();
        AttrDo attrDo = attrMapper.selectByPrimaryKey(attrId);
        if (flag){
            attrDo.setShowDesc((byte) 1);
        }else {
            attrDo.setShowDesc((byte) 0);
        }
        return attrMapper.updateByPrimaryKeySelective(attrDo);
    }

    @Override
    public int editAttrEnable(EditAttrEnableQuery editAttrEnableQuery) {
        //System.out.println(editAttrEnableQuery);
        Long attrId = editAttrEnableQuery.getAttrId();
        Boolean flag = editAttrEnableQuery.getFlag();
        AttrDo attrDo = attrMapper.selectByPrimaryKey(attrId);
        if (flag){
            attrDo.setEnable(1L);
        }else {
            attrDo.setEnable(0L);
        }
        return attrMapper.updateByPrimaryKeySelective(attrDo);
    }
}
