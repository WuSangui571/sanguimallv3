package com.sangui.sanguimall.member.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.constant.Constants;
import com.sangui.sanguimall.member.mapper.UmsGrowthChangeHistoryMapper;
import com.sangui.sanguimall.member.mapper.UmsIntegrationChangeHistoryMapper;
import com.sangui.sanguimall.member.model.query.ChangeHistoryPageQuery;
import com.sangui.sanguimall.member.model.vo.GrowthChangeHistoryVo;
import com.sangui.sanguimall.member.model.vo.IntegrationChangeHistoryVo;
import com.sangui.sanguimall.member.service.UmsMemberHistoryService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UmsMemberHistoryServiceImpl implements UmsMemberHistoryService {

    private static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UmsIntegrationChangeHistoryMapper integrationHistoryMapper;
    private final UmsGrowthChangeHistoryMapper growthHistoryMapper;

    public UmsMemberHistoryServiceImpl(UmsIntegrationChangeHistoryMapper integrationHistoryMapper,
            UmsGrowthChangeHistoryMapper growthHistoryMapper) {
        this.integrationHistoryMapper = integrationHistoryMapper;
        this.growthHistoryMapper = growthHistoryMapper;
    }

    @Override
    public PageInfo<IntegrationChangeHistoryVo> pageIntegrationHistory(
            ChangeHistoryPageQuery query) {
        int current = resolveCurrent(query);
        String keyword = trimKeyword(query);
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        List<IntegrationChangeHistoryVo> list = integrationHistoryMapper.selectWithMember(keyword);
        fillIntegrationTime(list);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<GrowthChangeHistoryVo> pageGrowthHistory(ChangeHistoryPageQuery query) {
        int current = resolveCurrent(query);
        String keyword = trimKeyword(query);
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        List<GrowthChangeHistoryVo> list = growthHistoryMapper.selectWithMember(keyword);
        fillGrowthTime(list);
        return new PageInfo<>(list);
    }

    private int resolveCurrent(ChangeHistoryPageQuery query) {
        if (query == null || query.getCurrent() == null || query.getCurrent() < 1) {
            return 1;
        }
        return query.getCurrent();
    }

    private String trimKeyword(ChangeHistoryPageQuery query) {
        if (query == null || query.getKeyword() == null) {
            return null;
        }
        String keyword = query.getKeyword().trim();
        return keyword.isEmpty() ? null : keyword;
    }

    private void fillIntegrationTime(List<IntegrationChangeHistoryVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(item -> item.setCreateTimeText(formatTime(item.getCreateTime())));
    }

    private void fillGrowthTime(List<GrowthChangeHistoryVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(item -> item.setCreateTimeText(formatTime(item.getCreateTime())));
    }

    private String formatTime(java.util.Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZONE_SHANGHAI);
        return FORMATTER.format(zonedDateTime);
    }
}
