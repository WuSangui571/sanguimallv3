package com.sangui.sanguimall.product.web;


import com.sangui.sanguimall.product.model.query.CategoryQuery;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.service.CategoryService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-29
 * @Description: 分类的 Controller
 * @Version: 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    CategoryService categoryService;

    @GetMapping("/list/tree")
    public R list(){
        List<CategoryVo> list = categoryService.listWithTree();
        return R.ok(list);
    }

    @GetMapping("/test")
    public R test(){
        System.out.println("pass");
        return R.ok(123);
    }

    @PostMapping("/add")
    public R add(CategoryQuery categoryQuery){
        //System.out.println(categoryQuery);
        CategoryVo categoryVo = categoryService.addCategory(categoryQuery);
        return R.ok(categoryVo);
    }

    @DeleteMapping("/del/{catId}")
    public R del(@PathVariable("catId") Long catId){
        int count = categoryService.delCategoryByCatId(catId);
        return count >= 1 ? R.ok() : R.fail();
    }
}
