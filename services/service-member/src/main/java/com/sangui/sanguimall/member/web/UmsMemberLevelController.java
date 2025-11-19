package com.sangui.sanguimall.member.web;

import com.sangui.sanguimall.member.service.UmsMemberLevelService;
import com.sangui.sanguimall.result.R;
import com.sangui.sanguimall.member.model.query.MemberLevelSaveQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/level")
public class UmsMemberLevelController {

    private final UmsMemberLevelService memberLevelService;

    public UmsMemberLevelController(UmsMemberLevelService memberLevelService) {
        this.memberLevelService = memberLevelService;
    }

    @GetMapping("/list")
    public R list() {
        return R.ok(memberLevelService.listAll());
    }

    @PostMapping
    public R create(@RequestBody MemberLevelSaveQuery query) {
        String validateMsg = validate(query, false);
        if (validateMsg != null) {
            return R.fail(validateMsg);
        }
        try {
            int count = memberLevelService.create(query);
            return count >= 1 ? R.ok() : R.fail();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    @PutMapping
    public R update(@RequestBody MemberLevelSaveQuery query) {
        String validateMsg = validate(query, true);
        if (validateMsg != null) {
            return R.fail(validateMsg);
        }
        try {
            int count = memberLevelService.update(query);
            return count >= 1 ? R.ok() : R.fail();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    private String validate(MemberLevelSaveQuery query, boolean requireId) {
        if (requireId && query.getId() == null) {
            return "ID 不能为空";
        }
        if (!StringUtils.hasText(query.getName())) {
            return "等级名称不能为空";
        }
        if (query.getGrowthPoint() == null || query.getGrowthPoint() < 0) {
            return "成长值需为非负整数";
        }
        if (query.getCommentGrowthPoint() == null || query.getCommentGrowthPoint() < 0) {
            return "评价成长值需为非负整数";
        }
        return null;
    }
}
