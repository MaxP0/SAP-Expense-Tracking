package org.maks.expensosap.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.Valid;

@Data
public class CreateExpenseReportDTO {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title is too long")
    private String title;

    @NotEmpty(message = "Report must contain at least one item")
    @Valid
    private List<CreateExpenseItemDTO> items;
}

