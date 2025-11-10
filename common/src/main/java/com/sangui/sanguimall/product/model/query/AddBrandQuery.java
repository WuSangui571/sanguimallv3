package com.sangui.sanguimall.product.model.query;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description: 新增品牌的 Query
 * @Version: 1.0
 */
@Data
public class AddBrandQuery {
    private Long id;
    private String name;
    private String logo;
    private Byte showStatus;
    private String firstLetter;
    private Integer sort;
    private String descript;
}
