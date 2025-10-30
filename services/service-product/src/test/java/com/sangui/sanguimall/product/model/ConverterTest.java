package com.sangui.sanguimall.product.model;


import com.sangui.sanguimall.product.model.converter.CategoryConverter;
import com.sangui.sanguimall.product.model.entity.CategoryDo;
import com.sangui.sanguimall.product.model.vo.CategoryVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: sangui
 * @CreateTime: 2025-10-30
 * @Description: 转换器的测试
 * @Version: 1.0
 */
@SpringBootTest
public class ConverterTest {
    @Resource
    private CategoryConverter categoryConverter;

    @Test
    public void testDoToVo(){
        CategoryDo categoryDo = new CategoryDo();
        categoryDo.setCatId(1L);
        // 调用方法转化
        CategoryVo categoryVo = categoryConverter.doToVo(categoryDo);

        System.out.println(categoryVo);
    }
}
