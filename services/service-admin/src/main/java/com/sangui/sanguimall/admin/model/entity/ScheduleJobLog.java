package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 定时任务日志
 * schedule_job_log
 */
@Data
public class ScheduleJobLog implements Serializable {
    /**
     * 任务日志id
     */
    private Long logId;

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * spring bean名称
     */
    private String beanName;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务状态    0：成功    1：失败
     */
    private Byte status;

    /**
     * 失败信息
     */
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    private Integer times;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}