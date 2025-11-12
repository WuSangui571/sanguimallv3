package com.sangui.sanguimall.product.web;


import com.sangui.sanguimall.product.model.query.CategoryQuery;
import com.sangui.sanguimall.product.model.query.CategorySequenceQuery;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import com.sangui.sanguimall.product.model.vo.OneTwoThreeCategoryVo;
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

    @PutMapping("/edit")
    public R edit(CategoryQuery categoryQuery){
        int count = categoryService.editCategory(categoryQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    @PutMapping("/sequence/edit")
    public R sequenceEdit(CategorySequenceQuery categorySequenceQuery){
        int count = categoryService.editCategorySequence(categorySequenceQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 获取一级分类的的数据
     * @return 一级分类的的数据
     */
    @GetMapping("/one")
    public R getOne(){
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = categoryService.getOne();
        return R.ok(oneTwoThreeCategoryVoList);
    }

    /**
     * 获取二级分类的的数据
     * @return 二级分类的的数据
     */
    @GetMapping("/two")
    public R getTwo(@RequestParam(value = "oneOptionsId")Long oneOptionsId){
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = categoryService.getTwo(oneOptionsId);
        return R.ok(oneTwoThreeCategoryVoList);
    }

    /**
     * 获取三级分类的的数据
     * @return 三级分类的的数据
     */
    @GetMapping("/three")
    public R getThree(@RequestParam(value = "twoOptionsId")Long twoOptionsId){
        List<OneTwoThreeCategoryVo> oneTwoThreeCategoryVoList = categoryService.getThree(twoOptionsId);
        return R.ok(oneTwoThreeCategoryVoList);
    }



}
