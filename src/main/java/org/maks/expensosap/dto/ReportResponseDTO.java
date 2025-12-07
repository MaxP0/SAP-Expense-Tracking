package org.maks.expensosap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportResponseDTO {
    private Long id;
    private String title;
    private UserShortDTO employee;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private List<ItemResponseDTO> items;
}
