package org.maks.expensosap.service;

import org.maks.expensosap.dto.CreateExpenseReportDTO;
import org.maks.expensosap.dto.ReportActionDTO;
import org.maks.expensosap.model.ExpenseItem;
import org.maks.expensosap.model.ExpenseReport;
import org.maks.expensosap.model.User;
import org.maks.expensosap.repository.ExpenseItemRepository;
import org.maks.expensosap.repository.ExpenseReportRepository;
import org.maks.expensosap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseReportService {

    private final ExpenseReportRepository reportRepo;
    private final ExpenseItemRepository itemRepo;
    private final UserRepository userRepo;
    private final LoggingService loggingService;

    public ExpenseReport createReport(Long userId, CreateExpenseReportDTO dto) {
        User user = userRepo.findById(userId).orElseThrow();

        ExpenseReport report = ExpenseReport.builder()
                .title(dto.getTitle())
                .employee(user)
                .status("submitted")
                .totalAmount(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        reportRepo.save(report);

        double total = 0;
        for (var it : dto.getItems()) {
            ExpenseItem item = ExpenseItem.builder()
                    .report(report)
                    .category(it.getCategory())
                    .description(it.getDescription())
                    .amount(it.getAmount())
                    .build();

            total += it.getAmount();
            itemRepo.save(item);
        }

        report.setTotalAmount(total);
        reportRepo.save(report);

        loggingService.log("report_created", userId, dto);

        return report;
    }

    public ExpenseReport getById(Long id) {
        return reportRepo.findById(id).orElseThrow();
    }

    public java.util.List<ExpenseReport> getPending() {
        return reportRepo.findByStatus("submitted");
    }

    public ExpenseReport applyAction(Long managerId, Long reportId, ReportActionDTO action) {
        ExpenseReport report = reportRepo.findById(reportId).orElseThrow();

        String newStatus = action.getAction().equals("approve") ? "approved" : "rejected";
        report.setStatus(newStatus);

        reportRepo.save(report);

        loggingService.log("report_" + newStatus, managerId, action);

        return report;
    }
}

