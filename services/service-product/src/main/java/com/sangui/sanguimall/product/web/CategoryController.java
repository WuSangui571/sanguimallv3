package com.sangui.sanguimall.product.web;


import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.service.CategoryService;
import com.sangui.sanguimall.result.R;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
