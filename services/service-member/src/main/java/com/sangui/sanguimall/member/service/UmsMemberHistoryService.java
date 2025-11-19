package com.sangui.sanguimall.member.service;

import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.member.model.query.ChangeHistoryPageQuery;
import com.sangui.sanguimall.member.model.vo.GrowthChangeHistoryVo;
import com.sangui.sanguimall.member.model.vo.IntegrationChangeHistoryVo;

public interface UmsMemberHistoryService {

    PageInfo<IntegrationChangeHistoryVo> pageIntegrationHistory(ChangeHistoryPageQuery query);

    PageInfo<GrowthChangeHistoryVo> pageGrowthHistory(ChangeHistoryPageQuery query);
}
