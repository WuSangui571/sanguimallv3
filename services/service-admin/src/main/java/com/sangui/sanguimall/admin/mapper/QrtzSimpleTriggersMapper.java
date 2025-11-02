package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzSimpleTriggers;
import com.sangui.sanguimall.admin.model.entity.QrtzSimpleTriggersKey;

public interface QrtzSimpleTriggersMapper {
    int deleteByPrimaryKey(QrtzSimpleTriggersKey key);

    int insert(QrtzSimpleTriggers record);

    int insertSelective(QrtzSimpleTriggers record);

    QrtzSimpleTriggers selectByPrimaryKey(QrtzSimpleTriggersKey key);

    int updateByPrimaryKeySelective(QrtzSimpleTriggers record);

    int updateByPrimaryKey(QrtzSimpleTriggers record);
}