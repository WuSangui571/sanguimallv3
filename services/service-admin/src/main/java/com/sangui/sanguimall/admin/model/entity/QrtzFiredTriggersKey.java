package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_fired_triggers
 */
@Data
public class QrtzFiredTriggersKey implements Serializable {
    private String schedName;

    private String entryId;

    private static final long serialVersionUID = 1L;
}