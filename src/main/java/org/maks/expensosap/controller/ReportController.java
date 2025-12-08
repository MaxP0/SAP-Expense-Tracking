package org.maks.expensosap.controller;

import org.maks.expensosap.dto.CreateExpenseReportDTO;
import org.maks.expensosap.dto.ReportActionDTO;
import org.maks.expensosap.dto.ReportResponseDTO;
import org.maks.expensosap.service.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ExpenseReportService service;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ReportResponseDTO> create(
            @PathVariable Long userId,
            @RequestBody CreateExpenseReportDTO dto
    ) {
        return ResponseEntity.ok(service.createReport(userId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ReportResponseDTO>> pending() {
        return ResponseEntity.ok(service.getPending());
    }

    @PostMapping("/{id}/action/{managerId}")
    public ResponseEntity<ReportResponseDTO> action(
            @PathVariable Long id,
            @PathVariable Long managerId,
            @RequestBody ReportActionDTO dto
    ) {
        return ResponseEntity.ok(service.applyAction(managerId, id, dto));
    }
}
