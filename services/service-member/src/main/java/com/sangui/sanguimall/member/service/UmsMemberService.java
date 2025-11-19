package com.sangui.sanguimall.member.service;

import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.member.model.query.MemberPageQuery;
import com.sangui.sanguimall.member.model.query.MemberSaveQuery;
import com.sangui.sanguimall.member.model.vo.UmsMemberVo;

public interface UmsMemberService {

    PageInfo<UmsMemberVo> pageMembers(MemberPageQuery query);

    UmsMemberVo getById(Long id);

    int create(MemberSaveQuery query);

    int update(MemberSaveQuery query);
}
