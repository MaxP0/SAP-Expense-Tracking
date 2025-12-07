package org.maks.expensosap.controller;

import org.maks.expensosap.dto.CreateExpenseReportDTO;
import org.maks.expensosap.dto.ReportActionDTO;
import org.maks.expensosap.model.ExpenseReport;
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

    // employee creates report
    @PostMapping("/create/{userId}")
    public ResponseEntity<ExpenseReport> create(
            @PathVariable Long userId,
            @RequestBody CreateExpenseReportDTO dto
    ) {
        ExpenseReport r = service.createReport(userId, dto);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseReport> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ExpenseReport>> pending() {
        return ResponseEntity.ok(service.getPending());
    }

    @PostMapping("/{id}/action/{managerId}")
    public ResponseEntity<ExpenseReport> action(
            @PathVariable Long id,
            @PathVariable Long managerId,
            @RequestBody ReportActionDTO dto
    ) {
        ExpenseReport r = service.applyAction(managerId, id, dto);
        return ResponseEntity.ok(r);
    }
}
