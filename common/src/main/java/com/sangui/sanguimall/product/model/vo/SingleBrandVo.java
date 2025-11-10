package com.sangui.sanguimall.product.model.vo;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-10
 * @Description: 单个的给前端的 Brand 的 Vo
 * @Version: 1.0
 */
@Data
public class SingleBrandVo {
    private Long id;
    private String name;
    private String logo;
    private Byte showStatus;
    private String firstLetter;
    private Integer sort;
    private String descript;
}
