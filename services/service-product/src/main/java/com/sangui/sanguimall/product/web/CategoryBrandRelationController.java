package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.vo.CategoryBrandRelationVo;
import com.sangui.sanguimall.product.service.CategoryBrandRelationService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public R getUsers(@RequestParam(value = "current", required = false) Integer current,
                      @RequestParam(value = "id") Long id) {
        if (current == null) {
            current = 1;
        }
        // 注意do 转 vo
        PageInfo<CategoryBrandRelationVo> pageInfo = categoryBrandRelationService.getRelationsById(current, id);

        return R.ok(pageInfo);
    }
}
