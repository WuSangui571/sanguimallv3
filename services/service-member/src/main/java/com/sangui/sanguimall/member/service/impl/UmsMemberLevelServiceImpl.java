package com.sangui.sanguimall.member.service.impl;

import com.sangui.sanguimall.member.mapper.UmsMemberLevelMapper;
import com.sangui.sanguimall.member.model.entity.UmsMemberLevel;
import com.sangui.sanguimall.member.model.vo.UmsMemberLevelVo;
import com.sangui.sanguimall.member.service.UmsMemberLevelService;
import com.sangui.sanguimall.member.model.query.MemberLevelSaveQuery;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {

    private final UmsMemberLevelMapper memberLevelMapper;

    public UmsMemberLevelServiceImpl(UmsMemberLevelMapper memberLevelMapper) {
        this.memberLevelMapper = memberLevelMapper;
    }

    @Override
    public List<UmsMemberLevelVo> listAll() {
        List<UmsMemberLevel> levels = memberLevelMapper.selectAll();
        if (levels == null || levels.isEmpty()) {
            return Collections.emptyList();
        }
        return levels.stream()
                .filter(Objects::nonNull)
                .map(this::toVo)
                .collect(Collectors.toList());
    }

    @Override
    public int create(MemberLevelSaveQuery query) {
        UmsMemberLevel level = toEntity(query);
        level.setId(null);
        return memberLevelMapper.insertSelective(level);
    }

    @Override
    public int update(MemberLevelSaveQuery query) {
        UmsMemberLevel level = toEntity(query);
        return memberLevelMapper.updateByPrimaryKeySelective(level);
    }

    private UmsMemberLevelVo toVo(UmsMemberLevel level) {
        UmsMemberLevelVo vo = new UmsMemberLevelVo();
        vo.setId(level.getId());
        vo.setName(level.getName());
        vo.setGrowthPoint(level.getGrowthPoint());
        vo.setDefaultLevel(asBool(level.getDefaultStatus()));
        vo.setFreeFreightPoint(level.getFreeFreightPoint());
        vo.setCommentGrowthPoint(level.getCommentGrowthPoint());
        vo.setPriviledgeFreeFreight(asBool(level.getPriviledgeFreeFreight()));
        vo.setPriviledgeMemberPrice(asBool(level.getPriviledgeMemberPrice()));
        vo.setPriviledgeBirthday(asBool(level.getPriviledgeBirthday()));
        vo.setNote(level.getNote());
        return vo;
    }

    private UmsMemberLevel toEntity(MemberLevelSaveQuery query) {
        UmsMemberLevel entity = new UmsMemberLevel();
        entity.setId(query.getId());
        entity.setName(query.getName());
        entity.setGrowthPoint(query.getGrowthPoint());
        entity.setDefaultStatus(query.getDefaultStatus());
        entity.setFreeFreightPoint(query.getFreeFreightPoint());
        entity.setCommentGrowthPoint(query.getCommentGrowthPoint());
        entity.setPriviledgeFreeFreight(query.getPriviledgeFreeFreight());
        entity.setPriviledgeMemberPrice(query.getPriviledgeMemberPrice());
        entity.setPriviledgeBirthday(query.getPriviledgeBirthday());
        entity.setNote(query.getNote());
        return entity;
    }

    private boolean asBool(Byte value) {
        return value != null && value == 1;
    }
}
