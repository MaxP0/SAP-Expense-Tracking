package org.maks.expensosap.dto;

import lombok.Data;

@Data
public class CreateExpenseItemDTO {
    private String category;
    private String description;
    private Double amount;
}

