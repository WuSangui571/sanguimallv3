package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzTriggers;
import com.sangui.sanguimall.admin.model.entity.QrtzTriggersKey;

public interface QrtzTriggersMapper {
    int deleteByPrimaryKey(QrtzTriggersKey key);

    int insert(QrtzTriggers record);

    int insertSelective(QrtzTriggers record);

    QrtzTriggers selectByPrimaryKey(QrtzTriggersKey key);

    int updateByPrimaryKeySelective(QrtzTriggers record);

    int updateByPrimaryKeyWithBLOBs(QrtzTriggers record);

    int updateByPrimaryKey(QrtzTriggers record);
}