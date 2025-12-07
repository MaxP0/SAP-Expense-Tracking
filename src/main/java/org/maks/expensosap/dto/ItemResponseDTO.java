package org.maks.expensosap.dto;

import lombok.Data;

@Data
public class ItemResponseDTO {
    private Long id;
    private String category;
    private String description;
    private Double amount;
}
