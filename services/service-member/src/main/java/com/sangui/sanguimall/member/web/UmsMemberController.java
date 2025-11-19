package com.sangui.sanguimall.member.web;

import com.sangui.sanguimall.member.model.query.MemberPageQuery;
import com.sangui.sanguimall.member.model.query.MemberSaveQuery;
import com.sangui.sanguimall.member.model.vo.UmsMemberVo;
import com.sangui.sanguimall.member.service.UmsMemberService;
import com.sangui.sanguimall.result.R;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class UmsMemberController {

    private final UmsMemberService memberService;

    public UmsMemberController(UmsMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public R list(MemberPageQuery query) {
        if (query == null) {
            query = new MemberPageQuery();
        }
        if (query.getCurrent() == null || query.getCurrent() < 1) {
            query.setCurrent(1);
        }
        return R.ok(memberService.pageMembers(query));
    }

    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Long id) {
        UmsMemberVo vo = memberService.getById(id);
        return vo == null ? R.fail("会员不存在") : R.ok(vo);
    }

    @PostMapping
    public R create(@RequestBody MemberSaveQuery query) {
        String validateMsg = validate(query, false);
        if (validateMsg != null) {
            return R.fail(validateMsg);
        }
        try {
            int count = memberService.create(query);
            return count >= 1 ? R.ok() : R.fail();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public R update(@PathVariable("id") Long id, @RequestBody MemberSaveQuery query) {
        query.setId(id);
        String validateMsg = validate(query, true);
        if (validateMsg != null) {
            return R.fail(validateMsg);
        }
        try {
            int count = memberService.update(query);
            return count >= 1 ? R.ok() : R.fail();
        } catch (IllegalArgumentException ex) {
            return R.fail(ex.getMessage());
        }
    }

    private String validate(MemberSaveQuery query, boolean requireId) {
        if (query == null) {
            return "??????";
        }
        if (requireId && query.getId() == null) {
            return "?? ID ????";
        }
        if (query.getLevelId() == null) {
            return "???????";
        }
        if (!StringUtils.hasText(query.getUsername())) {
            return "???????";
        }
        if (!requireId && !StringUtils.hasText(query.getPassword())) {
            return "??????";
        }
        if (StringUtils.hasText(query.getPassword())) {
            if (query.getPassword().length() < 6 || query.getPassword().length() > 20) {
                return "?????? 6-20 ?????";
            }
        }
        if (!StringUtils.hasText(query.getMobile())) {
            return "???????";
        }
        if (!StringUtils.hasText(query.getEmail())) {
            return "??????";
        }
        if (!StringUtils.hasText(query.getHeader())) {
            return "?????";
        }
        if (query.getStatus() == null) {
            return "?????";
        }
        if (query.getStatus() != 0 && query.getStatus() != 1) {
            return "??????";
        }
        if (query.getSourceType() != null && query.getSourceType() < 0) {
            return "????????";
        }
        if (query.getGender() != null && (query.getGender() < 0 || query.getGender() > 2)) {
            return "??????";
        }
        return null;
    }
}
