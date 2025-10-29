package com.sangui.sanguimall.product.mapper;

import com.sangui.sanguimall.product.model.entity.UndoLogDo;

public interface UndoLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UndoLogDo record);

    int insertSelective(UndoLogDo record);

    UndoLogDo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UndoLogDo record);

    int updateByPrimaryKeyWithBLOBs(UndoLogDo record);

    int updateByPrimaryKey(UndoLogDo record);
}