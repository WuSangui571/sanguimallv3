package com.sangui.sanguimall.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.product.mapper.AttrMapper;
import com.sangui.sanguimall.product.mapper.CategoryMapper;
import com.sangui.sanguimall.product.model.converter.AttrConverter;
import com.sangui.sanguimall.product.model.entity.AttrDo;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.query.attr.AddAttrQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrEnableQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrSearchTypeQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrShowDescQuery;
import com.sangui.sanguimall.product.model.vo.AttrDetailVo;
import com.sangui.sanguimall.product.model.vo.AttrForAttrTypeOptionVo;
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

    @Resource
    CategoryMapper categoryMapper;

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

    @Override
    public List<AttrForAttrTypeOptionVo> getAttrTypeOption() {
        List<AttrForAttrTypeOptionVo> list = new ArrayList<>();
        list.add(new AttrForAttrTypeOptionVo(0,"销售属性"));
        list.add(new AttrForAttrTypeOptionVo(1,"基本属性"));
        list.add(new AttrForAttrTypeOptionVo(2,"既是销售属性又是基本属性"));
        return list;
    }

    @Override
    public int addAttr(AddAttrQuery addAttrQuery) {
        System.out.println(addAttrQuery);
        AttrDo attrDo = new AttrDo();
        attrDo.setAttrName(addAttrQuery.getAttrName());
        attrDo.setSearchType(addAttrQuery.getSearchType());
        attrDo.setValueType(addAttrQuery.getValueType());

        String valueSelectWithDouHao = addAttrQuery.getValueSelect();
        if (valueSelectWithDouHao != null) {
            String valueSelect = valueSelectWithDouHao.replace(",", ";");
            attrDo.setValueSelect(valueSelect);
        }


        attrDo.setIcon(addAttrQuery.getIcon());
        attrDo.setAttrType(addAttrQuery.getAttrType());
        attrDo.setEnable(addAttrQuery.getEnable());
        attrDo.setCatelogId(addAttrQuery.getCatelogId());
        attrDo.setShowDesc(addAttrQuery.getShowDesc());

        return attrMapper.insertSelective(attrDo);
    }

    @Override
    public AttrDetailVo getAttrDetail(Long attrId) {
        AttrDo attrDo = attrMapper.selectByPrimaryKey(attrId);
        if (attrDo == null) {
            return null;
        }
        AttrDetailVo vo = new AttrDetailVo();
        vo.setAttrId(attrDo.getAttrId());
        vo.setAttrName(attrDo.getAttrName());
        vo.setSearchType(attrDo.getSearchType());
        vo.setValueType(attrDo.getValueType());
        vo.setIcon(attrDo.getIcon());
        vo.setValueSelect(attrDo.getValueSelect());
        vo.setAttrType(attrDo.getAttrType());
        vo.setEnable(attrDo.getEnable());
        vo.setCatelogId(attrDo.getCatelogId());
        vo.setShowDesc(attrDo.getShowDesc());
        vo.setThreeCategoryId(attrDo.getCatelogId());

        CategoryDo level3 = categoryMapper.selectByPrimaryKey(attrDo.getCatelogId());
        if (level3 != null) {
            vo.setTwoCategoryId(level3.getParentCid());
            CategoryDo level2 = categoryMapper.selectByPrimaryKey(level3.getParentCid());
            if (level2 != null) {
                vo.setOneCategoryId(level2.getParentCid());
            }
        }
        return vo;
    }

    @Override
    public int editAttr(AddAttrQuery addAttrQuery) {
        if (addAttrQuery.getId() == null) {
            return 0;
        }
        AttrDo attrDo = attrMapper.selectByPrimaryKey(addAttrQuery.getId());
        if (attrDo == null) {
            return 0;
        }
        attrDo.setAttrName(addAttrQuery.getAttrName());
        attrDo.setSearchType(addAttrQuery.getSearchType());
        attrDo.setValueType(addAttrQuery.getValueType());
        attrDo.setIcon(addAttrQuery.getIcon());
        attrDo.setAttrType(addAttrQuery.getAttrType());
        attrDo.setEnable(addAttrQuery.getEnable());
        attrDo.setCatelogId(addAttrQuery.getCatelogId());
        attrDo.setShowDesc(addAttrQuery.getShowDesc());
        if (addAttrQuery.getValueSelect() != null) {
            String valueSelect = addAttrQuery.getValueSelect().replace(",", ";");
            attrDo.setValueSelect(valueSelect);
        }
        return attrMapper.updateByPrimaryKeySelective(attrDo);
    }

    @Override
    public int delAttrById(Long id) {
        return attrMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delAttrByIds(String ids) {
        return attrMapper.deleteByIds(ids);
    }
}
