package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_locks
 */
@Data
public class QrtzLocksKey implements Serializable {
    private String schedName;

    private String lockName;

    private static final long serialVersionUID = 1L;
}