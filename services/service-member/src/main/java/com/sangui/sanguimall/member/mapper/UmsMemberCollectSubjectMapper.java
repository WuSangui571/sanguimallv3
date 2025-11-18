package com.sangui.sanguimall.member.mapper;

import com.sangui.sanguimall.member.model.entity.UmsMemberCollectSubject;

public interface UmsMemberCollectSubjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberCollectSubject record);

    int insertSelective(UmsMemberCollectSubject record);

    UmsMemberCollectSubject selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsMemberCollectSubject record);

    int updateByPrimaryKey(UmsMemberCollectSubject record);
}