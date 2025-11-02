package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_cron_triggers
 */
@Data
public class QrtzCronTriggers extends QrtzCronTriggersKey implements Serializable {
    private String cronExpression;

    private String timeZoneId;

    private static final long serialVersionUID = 1L;
}