package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_paused_trigger_grps
 */
@Data
public class QrtzPausedTriggerGrpsKey implements Serializable {
    private String schedName;

    private String triggerGroup;

    private static final long serialVersionUID = 1L;
}