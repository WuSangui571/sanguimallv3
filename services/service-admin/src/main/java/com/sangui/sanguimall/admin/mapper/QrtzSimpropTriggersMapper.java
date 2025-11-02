package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzSimpropTriggers;
import com.sangui.sanguimall.admin.model.entity.QrtzSimpropTriggersKey;

public interface QrtzSimpropTriggersMapper {
    int deleteByPrimaryKey(QrtzSimpropTriggersKey key);

    int insert(QrtzSimpropTriggers record);

    int insertSelective(QrtzSimpropTriggers record);

    QrtzSimpropTriggers selectByPrimaryKey(QrtzSimpropTriggersKey key);

    int updateByPrimaryKeySelective(QrtzSimpropTriggers record);

    int updateByPrimaryKey(QrtzSimpropTriggers record);
}