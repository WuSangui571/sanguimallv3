package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_job_details
 */
@Data
public class QrtzJobDetailsKey implements Serializable {
    private String schedName;

    private String jobName;

    private String jobGroup;

    private static final long serialVersionUID = 1L;
}