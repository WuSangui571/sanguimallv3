package com.sangui.sanguimall.admin.mapper;

import com.sangui.sanguimall.admin.model.entity.QrtzJobDetails;
import com.sangui.sanguimall.admin.model.entity.QrtzJobDetailsKey;

public interface QrtzJobDetailsMapper {
    int deleteByPrimaryKey(QrtzJobDetailsKey key);

    int insert(QrtzJobDetails record);

    int insertSelective(QrtzJobDetails record);

    QrtzJobDetails selectByPrimaryKey(QrtzJobDetailsKey key);

    int updateByPrimaryKeySelective(QrtzJobDetails record);

    int updateByPrimaryKeyWithBLOBs(QrtzJobDetails record);

    int updateByPrimaryKey(QrtzJobDetails record);
}