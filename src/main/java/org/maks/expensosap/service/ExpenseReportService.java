package org.maks.expensosap.service;

import org.maks.expensosap.dto.*;
import org.maks.expensosap.model.ExpenseItem;
import org.maks.expensosap.model.ExpenseReport;
import org.maks.expensosap.model.User;
import org.maks.expensosap.repository.ExpenseItemRepository;
import org.maks.expensosap.repository.ExpenseReportRepository;
import org.maks.expensosap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseReportService {

    private final ExpenseReportRepository reportRepo;
    private final ExpenseItemRepository itemRepo;
    private final UserRepository userRepo;
    private final LoggingService loggingService;

    public ReportResponseDTO createReport(Long userId, CreateExpenseReportDTO dto) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseReport report = ExpenseReport.builder()
                .title(dto.getTitle())
                .employee(user)
                .status("submitted")
                .totalAmount(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        report = reportRepo.save(report);

        double total = 0;
        if (dto.getItems() != null) {
            for (var it : dto.getItems()) {
                ExpenseItem item = ExpenseItem.builder()
                        .report(report)
                        .category(it.getCategory())
                        .description(it.getDescription())
                        .amount(it.getAmount())
                        .build();
                itemRepo.save(item);

                total += it.getAmount();
            }
        }

        report.setTotalAmount(total);
        report = reportRepo.save(report);

        loggingService.log("report_created", userId, dto);

        return toReportDTO(report);
    }

    public ReportResponseDTO getById(Long id) {
        ExpenseReport r = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return toReportDTO(r);
    }

    public List<ReportResponseDTO> getPending() {
        return reportRepo.findByStatus("submitted")
                .stream()
                .map(this::toReportDTO)
                .toList();
    }

    public ReportResponseDTO applyAction(Long managerId, Long reportId, ReportActionDTO action) {
        ExpenseReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        String act = action.getAction();
        if ("approve".equalsIgnoreCase(act)) {
            report.setStatus("approved");
        } else if ("reject".equalsIgnoreCase(act)) {
            report.setStatus("rejected");
        } else {
            throw new RuntimeException("Unknown action");
        }

        report = reportRepo.save(report);
        loggingService.log("report_" + report.getStatus(), managerId, action);

        return toReportDTO(report);
    }

    private UserShortDTO toUserShortDTO(org.maks.expensosap.model.User user) {
        UserShortDTO dto = new UserShortDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    private ItemResponseDTO toItemDTO(org.maks.expensosap.model.ExpenseItem item) {
        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setCategory(item.getCategory());
        dto.setDescription(item.getDescription());
        dto.setAmount(item.getAmount());
        return dto;
    }

    private ReportResponseDTO toReportDTO(org.maks.expensosap.model.ExpenseReport report) {
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(report.getId());
        dto.setTitle(report.getTitle());
        dto.setStatus(report.getStatus());
        dto.setTotalAmount(report.getTotalAmount());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setEmployee(toUserShortDTO(report.getEmployee()));

        dto.setItems(
                (report.getItems() == null ? List.<ExpenseItem>of() : report.getItems())
                        .stream()
                        .map(this::toItemDTO)
                        .toList()
        );

        return dto;
    }
}

