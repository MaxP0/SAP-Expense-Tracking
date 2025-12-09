package org.maks.expensosap.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@Data
public class CreateExpenseItemDTO {

    @NotBlank(message = "Category cannot be empty")
    @Size(max = 50)
    private String category;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 200)
    private String description;

    @Positive(message = "Amount must be positive")
    private double amount;
}

