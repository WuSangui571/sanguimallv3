package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.query.BrandEditShowStatusQuery;
import com.sangui.sanguimall.product.service.BrandService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-07
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Resource
    BrandService brandService;

    @GetMapping("/brand")
    public R getBrandsByPage(@RequestParam(value = "current",required = false)Integer current){
        if (current == null){
            current = 1;
        }
        PageInfo<BrandDo> pageInfo = brandService.getBrandsByPage(current);

        return R.ok(pageInfo);
    }

    /**
     * 编辑品牌的是否显示
     * @param brandEditShowStatusQuery 前端传过来的用户信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/brandStatus")
    public R editBrandShowStatus(BrandEditShowStatusQuery brandEditShowStatusQuery) {
        int count = brandService.editBrandShowStatus(brandEditShowStatusQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

}
