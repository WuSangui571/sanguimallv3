package com.sangui.sanguimall.member.model.query;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MemberLevelSaveQuery {
    private Long id;
    private String name;
    private Integer growthPoint;
    private Byte defaultStatus;
    private BigDecimal freeFreightPoint;
    private Integer commentGrowthPoint;
    private Byte priviledgeFreeFreight;
    private Byte priviledgeMemberPrice;
    private Byte priviledgeBirthday;
    private String note;
}
