package com.sangui.sanguimall.admin.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * undo_log
 */
@Data
public class UndoLog implements Serializable {
    private Long id;

    private Long branchId;

    private String xid;

    private String context;

    private Integer logStatus;

    private Date logCreated;

    private Date logModified;

    private String ext;

    private byte[] rollbackInfo;

    private static final long serialVersionUID = 1L;
}