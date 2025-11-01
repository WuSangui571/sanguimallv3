package com.sangui.sanguimall.product.model.vo;


import lombok.Data;

/**
 * @Author: sangui
 * @CreateTime: 2025-11-01
 * @Description: 前端返回给后端的调整种类顺序的对象
 * @Version: 1.0
 */
@Data
public class CategorySequenceQuery {
    /**
     * 无 CategoryDO 里的对应
     */
    private Long id;
    /**
     * 对应 CategoryDO 里的 catId 属性，即分类 id，这里指的是要拖动的节点的 id
     */
    private Long draggingNodeId;
    /**
     * 对应 CategoryDO 里的 catId 属性，即分类 id，这里指的是要将节点拖动到此节点之后
     */
    private Long afterDropNodeId;


}
