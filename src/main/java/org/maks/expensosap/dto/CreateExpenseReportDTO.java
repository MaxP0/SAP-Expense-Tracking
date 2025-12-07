package org.maks.expensosap.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateExpenseReportDTO {
    private String title;
    private List<CreateExpenseItemDTO> items;
}
