package com.sangui.sanguimall.member.mapper;

import com.sangui.sanguimall.member.model.entity.UmsMemberLevel;
import java.util.List;

public interface UmsMemberLevelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberLevel record);

    int insertSelective(UmsMemberLevel record);

    UmsMemberLevel selectByPrimaryKey(Long id);

    List<UmsMemberLevel> selectAll();

    int updateByPrimaryKeySelective(UmsMemberLevel record);

    int updateByPrimaryKey(UmsMemberLevel record);

    int countDefaultLevel(Long excludeId);
}
