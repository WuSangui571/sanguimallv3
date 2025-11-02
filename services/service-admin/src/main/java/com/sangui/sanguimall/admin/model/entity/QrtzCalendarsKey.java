package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * qrtz_calendars
 */
@Data
public class QrtzCalendarsKey implements Serializable {
    private String schedName;

    private String calendarName;

    private static final long serialVersionUID = 1L;
}