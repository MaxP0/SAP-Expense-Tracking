package org.maks.expensosap.repository;

import org.maks.expensosap.model.ExpenseReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {
    List<ExpenseReport> findByStatus(String status);
}
