package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzCalendars;
import com.sangui.sanguimall.admin.model.entity.QrtzCalendarsKey;

public interface QrtzCalendarsMapper {
    int deleteByPrimaryKey(QrtzCalendarsKey key);

    int insert(QrtzCalendars record);

    int insertSelective(QrtzCalendars record);

    QrtzCalendars selectByPrimaryKey(QrtzCalendarsKey key);

    int updateByPrimaryKeySelective(QrtzCalendars record);

    int updateByPrimaryKeyWithBLOBs(QrtzCalendars record);
}