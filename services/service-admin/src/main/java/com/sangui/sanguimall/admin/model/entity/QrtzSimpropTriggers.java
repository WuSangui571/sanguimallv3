package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * qrtz_simprop_triggers
 */
@Data
public class QrtzSimpropTriggers extends QrtzSimpropTriggersKey implements Serializable {
    private String strProp1;

    private String strProp2;

    private String strProp3;

    private Integer intProp1;

    private Integer intProp2;

    private Long longProp1;

    private Long longProp2;

    private BigDecimal decProp1;

    private BigDecimal decProp2;

    private String boolProp1;

    private String boolProp2;

    private static final long serialVersionUID = 1L;
}