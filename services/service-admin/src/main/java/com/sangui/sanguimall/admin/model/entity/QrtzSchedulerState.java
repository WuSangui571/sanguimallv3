package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_scheduler_state
 */
@Data
public class QrtzSchedulerState extends QrtzSchedulerStateKey implements Serializable {
    private Long lastCheckinTime;

    private Long checkinInterval;

    private static final long serialVersionUID = 1L;
}