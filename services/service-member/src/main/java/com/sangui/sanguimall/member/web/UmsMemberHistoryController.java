package com.sangui.sanguimall.member.web;

import com.sangui.sanguimall.member.model.query.ChangeHistoryPageQuery;
import com.sangui.sanguimall.member.service.UmsMemberHistoryService;
import com.sangui.sanguimall.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class UmsMemberHistoryController {

    private final UmsMemberHistoryService memberHistoryService;

    public UmsMemberHistoryController(UmsMemberHistoryService memberHistoryService) {
        this.memberHistoryService = memberHistoryService;
    }

    @GetMapping("/integration")
    public R integration(ChangeHistoryPageQuery query) {
        return R.ok(memberHistoryService.pageIntegrationHistory(query));
    }

    @GetMapping("/growth")
    public R growth(ChangeHistoryPageQuery query) {
        return R.ok(memberHistoryService.pageGrowthHistory(query));
    }
}
