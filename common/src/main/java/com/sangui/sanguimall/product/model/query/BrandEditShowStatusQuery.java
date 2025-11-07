package com.sangui.sanguimall.product.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-07
 * @Description: 前端传给后端的，仅修改 ShowStatus 的 BrandQuery
 * @Version: 1.0
 */
@Data
public class BrandEditShowStatusQuery {
    private Long brandId;
    private Boolean flag;
}
