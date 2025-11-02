package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_blob_triggers
 */
@Data
public class QrtzBlobTriggersKey implements Serializable {
    private String schedName;

    private String triggerName;

    private String triggerGroup;

    private static final long serialVersionUID = 1L;
}