package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_scheduler_state
 */
@Data
public class QrtzSchedulerStateKey implements Serializable {
    private String schedName;

    private String instanceName;

    private static final long serialVersionUID = 1L;
}