package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_simple_triggers
 */
@Data
public class QrtzSimpleTriggers extends QrtzSimpleTriggersKey implements Serializable {
    private Long repeatCount;

    private Long repeatInterval;

    private Long timesTriggered;

    private static final long serialVersionUID = 1L;
}