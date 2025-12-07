package org.maks.expensosap.dto;

import lombok.Data;

@Data
public class ReportActionDTO {
    private String action;   // "approve" or "reject"
    private String comment;
}
