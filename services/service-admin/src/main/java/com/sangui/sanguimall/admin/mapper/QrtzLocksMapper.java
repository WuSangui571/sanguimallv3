package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzLocksKey;

public interface QrtzLocksMapper {
    int deleteByPrimaryKey(QrtzLocksKey key);

    int insert(QrtzLocksKey record);

    int insertSelective(QrtzLocksKey record);
}