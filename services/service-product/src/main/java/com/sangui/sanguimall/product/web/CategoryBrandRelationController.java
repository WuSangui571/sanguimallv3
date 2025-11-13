package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.query.CategoryBrandRelationQuery;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;
import com.sangui.sanguimall.product.service.CategoryBrandRelationService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-12
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/categoryBrandRelation")
public class CategoryBrandRelationController {
    @Resource
    CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 获取全部种类-品牌关联信息的信息
     *
     * @param current 当前页数
     * @return 当前页数的信息
     */
    @GetMapping("/relations")
    public R getRelationsById(@RequestParam(value = "current", required = false) Integer current,
                      @RequestParam(value = "id") Long id) {
        if (current == null) {
            current = 1;
        }
        // 注意 do 转 vo
        PageInfo<CategoryBrandRelationVo> pageInfo = categoryBrandRelationService.getRelationsById(current, id);

        return R.ok(pageInfo);
    }

    /**
     * 新增分类
     * @param categoryBrandRelationQuery 前端传过来的新增信息
     * @return 响应前端 o 不 ok
     */
    @PostMapping("/relation")
    public R addUser(CategoryBrandRelationQuery categoryBrandRelationQuery) {
        int count = categoryBrandRelationService.addRelation(categoryBrandRelationQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 删除
     * @param id 前端传过来的指定 id 的
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/relation/{id}")
    public R delRelationById(@PathVariable("id") Long id) {
        int count = categoryBrandRelationService.delRelationById(id);
        return count >= 1 ? R.ok() : R.fail();
    }
}
