package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzPausedTriggerGrpsKey;

public interface QrtzPausedTriggerGrpsMapper {
    int deleteByPrimaryKey(QrtzPausedTriggerGrpsKey key);

    int insert(QrtzPausedTriggerGrpsKey record);

    int insertSelective(QrtzPausedTriggerGrpsKey record);
}