package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.AttrGroupDo;
import com.sangui.sanguimall.product.model.query.AttrGroupQuery;
import com.sangui.sanguimall.product.model.vo.AttrGroupVo;
import com.sangui.sanguimall.product.service.AttrGroupService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description:
 * @Version: 1.0
 */
@RequestMapping("/attrGroup")
@RestController
public class AttrGroupController {
    @Resource
    AttrGroupService attrGroupService;

    /**
     * 获取全部的属性分组信息
     * @param current 当前页数
     * @return 当前页数的属性分组信息
     */
    @GetMapping("/attrGroups")
    public R getAttrGroupsByPage(@RequestParam(value = "current",required = false)Integer current,
        @RequestParam(value = "catelogId")Long catelogId){
        if (current == null){
            current = 1;
        }
        PageInfo<AttrGroupVo> pageInfo = attrGroupService.getAttrGroupsByPage(current,catelogId);

        return R.ok(pageInfo);
    }

    /**
     * 新增属性分组
     * @param attrGroupQuery 前端传过来的属性分组信息
     * @return 响应前端 o 不 ok
     */
    @PostMapping("/attrGroup")
    public R addAttrGroup(AttrGroupQuery attrGroupQuery) {
        int count = attrGroupService.addAttrGroup(attrGroupQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 响应给前端指定 id 的属性分组信息
     * @param id 属性分组 id
     * @return 属性分组信息
     */
    @GetMapping("/attrGroup/{id}")
    public R getSingleAttrGroupById(@PathVariable("id")Long id){
        AttrGroupVo attrGroupVo = attrGroupService.getSingleAttrGroupById(id);
        return R.ok(attrGroupVo);
    }

    /**
     * 编辑属性分组
     * @param attrGroupQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/attrGroup")
    public R editAttrGroup(AttrGroupQuery attrGroupQuery) {
        int count = attrGroupService.editAttrGroup(attrGroupQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 删除属性分组
     * @param id 前端传过来的指定 id 的属性分组
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/attrGroup/{id}")
    public R delAttrGroupById(@PathVariable("id") Long id) {
        int count = attrGroupService.delAttrGroupById(id);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 批量删除属性分组
     * @param ids id 字符串，类似 "2,4,5"
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/attrGroups")
    public R batchDelAttrGroupByIds(@RequestParam(value = "ids", required = false) String ids) {
        int count = attrGroupService.batchDelAttrGroupByIds(ids);
        // System.out.println(ids);
        int len = ids.split(",").length;
        return count >= len ? R.ok() : R.fail();
    }
}
