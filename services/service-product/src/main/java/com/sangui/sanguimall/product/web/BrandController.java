package com.sangui.sanguimall.product.web;


import com.github.pagehelper.PageInfo;
import com.sangui.sanguimall.product.model.entity.BrandDo;
import com.sangui.sanguimall.product.model.query.AddBrandQuery;
import com.sangui.sanguimall.product.model.query.BrandEditShowStatusQuery;
import com.sangui.sanguimall.product.model.vo.SingleBrandVo;
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

    /**
     * 新增品牌
     * @param addBrandQuery 前端传过来的品牌信息
     * @return 响应前端 o 不 ok
     */
    @PostMapping("/brand")
    public R addUser(AddBrandQuery addBrandQuery) {
        int count = brandService.addBrand(addBrandQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 响应给前端指定 id 的品牌信息
     * @param id 品牌 id
     * @return 品牌信息
     */
    @GetMapping("/brand/{id}")
    public R getSingeBrandById(@PathVariable("id")Long id){
        SingleBrandVo singleBrandVo = brandService.getSingeBrandById(id);
        return R.ok(singleBrandVo);
    }

    /**
     * 编辑品牌
     * @param addBrandQuery 前端传过来的品牌信息
     * @return 响应前端 o 不 ok
     */
    @PutMapping("/brand")
    public R editUser(AddBrandQuery addBrandQuery) {
        int count = brandService.editBrand(addBrandQuery);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 删除品牌
     * @param id 前端传过来的指定 id 的品牌
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/brand/{id}")
    public R delBrandById(@PathVariable("id") Long id) {
        int count = brandService.delBrandById(id);
        return count >= 1 ? R.ok() : R.fail();
    }

    /**
     * 批量删除品牌
     * @param ids id 字符串，类似 "2,4,5"
     * @return 响应前端 o 不 ok
     */
    @DeleteMapping("/brands")
    public R delBrandByIds(@RequestParam(value = "ids", required = false) String ids) {
        System.out.println("前端接收到的ids=" +ids);
        int count = brandService.delBrandByIds(ids);
        // System.out.println(ids);
        int len = ids.split(",").length;
        return count >= len ? R.ok() : R.fail();
    }

    @GetMapping("/searchBrand")
    public R getBrandsBySelect(@RequestParam(value = "current",required = false)Integer current,
                              @RequestParam(value = "selectValue")String selectValue){
        if (current == null){
            current = 1;
        }
        PageInfo<BrandDo> pageInfo = brandService.getBrandsBySelect(current,selectValue);

        return R.ok(pageInfo);
    }
}
