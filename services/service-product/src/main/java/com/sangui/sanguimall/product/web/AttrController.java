package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.query.attr.EditAttrEnableQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrSearchTypeQuery;
import com.sangui.sanguimall.product.model.query.attr.EditAttrShowDescQuery;
import com.sangui.sanguimall.product.model.vo.AttrForAttrTypeOptionVo;
import com.sangui.sanguimall.product.model.vo.AttrVo;
import com.sangui.sanguimall.product.service.AttrService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-16
 * @Description:
 * @Version: 1.0
 */
@RequestMapping("/attr")
@RestController
public class AttrController {
    @Resource
    AttrService attrService;

    /**
     * 获取全部的 Attr 信息
     * @param current 当前页数
     * @return 当前页数的 Attr 信息
     */
    @GetMapping("/attrs")
    public R getUsers(@RequestParam(value = "current",required = false)Integer current){
        if (current == null){
            current = 1;
        }
        // 注意do 转 vo
        PageInfo<AttrVo> pageInfo = attrService.getAttrsByPage(current);

        return R.ok(pageInfo);
    }

    /**
     * 编辑 searchType 的是否显示
     * @param editAttrSearchTypeQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/searchType")
    public R editAttrSearchType(EditAttrSearchTypeQuery editAttrSearchTypeQuery) {
        int count = attrService.editAttrSearchType(editAttrSearchTypeQuery);
        return count >= 1 ? R.ok() : R.fail();
    }


    /**
     * 编辑 快速显示
     * @param editAttrShowDescQuery 前端传过来的
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/showDesc")
    public R editAttrShowDesc(EditAttrShowDescQuery editAttrShowDescQuery) {
        int count = attrService.editAttrShowDesc(editAttrShowDescQuery);
        return count >= 1 ? R.ok() : R.fail();
    }


    @PutMapping("/enable")
    public R editAttrEnable(EditAttrEnableQuery editAttrEnableQuery) {
        int count = attrService.editAttrEnable(editAttrEnableQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    @GetMapping("/attrTypeOption")
    public R getAttrTypeOption(){
        List<AttrForAttrTypeOptionVo> list = attrService.getAttrTypeOption();
        return R.ok(list);
    }
}
