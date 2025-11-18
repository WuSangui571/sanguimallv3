package com.sangui.sanguimall.product.service.impl;

import com.sangui.sanguimall.product.mapper.AttrAttrgroupRelationMapper;
import com.sangui.sanguimall.product.mapper.AttrGroupMapper;
import com.sangui.sanguimall.product.mapper.AttrMapper;
import com.sangui.sanguimall.product.model.converter.AttrConverter;
import com.sangui.sanguimall.product.model.entity.AttrAttrgroupRelationDo;
import com.sangui.sanguimall.product.model.entity.AttrDo;
import com.sangui.sanguimall.product.model.entity.AttrGroupDo;
import com.sangui.sanguimall.product.model.vo.AttrVo;
import com.sangui.sanguimall.product.service.AttrAttrgroupRelationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AttrAttrgroupRelationServiceImpl implements AttrAttrgroupRelationService {

    @Resource
    AttrMapper attrMapper;

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Resource
    AttrAttrgroupRelationMapper attrAttrgroupRelationMapper;

    @Resource
    AttrConverter attrConverter;

    @Override
    public List<AttrVo> listUnrelatedAttrs(Long catelogId) {
        if (catelogId == null) {
            return new ArrayList<>();
        }
        List<AttrDo> attrDos = attrMapper.selectUnrelatedAttrsByCatelogId(catelogId);
        List<AttrVo> res = new ArrayList<>();
        for (AttrDo attrDo : attrDos) {
            res.add(attrConverter.doToVo(attrDo));
        }
        return res;
    }

    @Override
    public int addRelation(Long attrGroupId, Long attrId) {
        if (attrGroupId == null || attrId == null) {
            return 0;
        }
        AttrGroupDo attrGroupDo = attrGroupMapper.selectByPrimaryKey(attrGroupId);
        AttrDo attrDo = attrMapper.selectByPrimaryKey(attrId);
        if (attrGroupDo == null || attrDo == null) {
            return 0;
        }
        if (!Objects.equals(attrDo.getCatelogId(), attrGroupDo.getCatelogId())) {
            return 0;
        }
        AttrAttrgroupRelationDo existed = attrAttrgroupRelationMapper.selectByAttrId(attrId);
        if (existed != null) {
            return 0;
        }
        AttrAttrgroupRelationDo relationDo = new AttrAttrgroupRelationDo();
        relationDo.setAttrGroupId(attrGroupId);
        relationDo.setAttrId(attrId);
        relationDo.setAttrSort(0);
        return attrAttrgroupRelationMapper.insertSelective(relationDo);
    }

    @Override
    public List<AttrVo> listRelatedAttrs(Long attrGroupId) {
        if (attrGroupId == null) {
            return new ArrayList<>();
        }
        List<AttrDo> attrDos = attrMapper.selectRelatedAttrsByGroupId(attrGroupId);
        List<AttrVo> res = new ArrayList<>();
        for (AttrDo attrDo : attrDos) {
            res.add(attrConverter.doToVo(attrDo));
        }
        return res;
    }

    @Override
    public int removeRelation(Long attrGroupId, Long attrId) {
        if (attrGroupId == null || attrId == null) {
            return 0;
        }
        return attrAttrgroupRelationMapper.deleteByAttrGroupAndAttr(attrGroupId, attrId);
    }
}
