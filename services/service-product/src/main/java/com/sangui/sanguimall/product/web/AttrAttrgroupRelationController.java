package com.sangui.sanguimall.product.web;


import com.sangui.sanguimall.product.service.AttrAttrgroupRelationService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 属性-属性分组关联控制器
 */
@RestController
@RequestMapping("/attrGroupRelation")
public class AttrAttrgroupRelationController {

    @Resource
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 查询未关联的规格参数列表
     */
    @GetMapping("/attrs")
    public R listAvailableAttrs(@RequestParam("catelogId") Long catelogId) {
        return R.ok(attrAttrgroupRelationService.listUnrelatedAttrs(catelogId));
    }

    /**
     * 建立关联
     */
    @PostMapping("/relation")
    public R addRelation(@RequestParam("attrGroupId") Long attrGroupId,
                         @RequestParam("attrId") Long attrId) {
        int count = attrAttrgroupRelationService.addRelation(attrGroupId, attrId);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 查询指定分组已关联的规格参数
     */
    @GetMapping("/related")
    public R listRelated(@RequestParam("attrGroupId") Long attrGroupId) {
        return R.ok(attrAttrgroupRelationService.listRelatedAttrs(attrGroupId));
    }

    /**
     * 删除指定分组与规格参数的关联
     */
    @PostMapping("/relation/delete")
    public R deleteRelation(@RequestParam("attrGroupId") Long attrGroupId,
                            @RequestParam("attrId") Long attrId) {
        int count = attrAttrgroupRelationService.removeRelation(attrGroupId, attrId);
        return count >= 1 ? R.ok() : R.fail();
    }
}
