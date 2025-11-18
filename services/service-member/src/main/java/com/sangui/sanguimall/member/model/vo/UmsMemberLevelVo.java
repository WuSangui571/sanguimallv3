package com.sangui.sanguimall.member.model.vo;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 会员等级视图对象
 */
@Data
public class UmsMemberLevelVo {
    private Long id;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 成长值门槛
     */
    private Integer growthPoint;

    /**
     * 是否默认等级
     */
    private boolean defaultLevel;

    /**
     * 免邮门槛
     */
    private BigDecimal freeFreightPoint;

    /**
     * 每次评价获取的成长值
     */
    private Integer commentGrowthPoint;

    /**
     * 是否享受免邮特权
     */
    private boolean priviledgeFreeFreight;

    /**
     * 是否享受会员价格特权
     */
    private boolean priviledgeMemberPrice;

    /**
     * 是否享受生日特权
     */
    private boolean priviledgeBirthday;

    /**
     * 备注
     */
    private String note;
}
