package com.sangui.sanguimall.member.service;

import com.sangui.sanguimall.member.model.vo.UmsMemberLevelVo;
import com.sangui.sanguimall.member.model.query.MemberLevelSaveQuery;
import java.util.List;

public interface UmsMemberLevelService {
    /**
     * 查询所有会员等级信息，按成长值从低到高排列
     *
     * @return 会员等级列表
     */
    List<UmsMemberLevelVo> listAll();

    /**
     * 新增会员等级
     * @param query 请求参数
     * @return 影响行数
     */
    int create(MemberLevelSaveQuery query);

    /**
     * 编辑会员等级
     * @param query 请求参数（包含 id）
     * @return 影响行数
     */
    int update(MemberLevelSaveQuery query);
}
