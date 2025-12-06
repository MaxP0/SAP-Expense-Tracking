package org.maks.expensosap.repository;

import org.maks.expensosap.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {
    List<ExpenseItem> findByReportId(Long reportId);
}

