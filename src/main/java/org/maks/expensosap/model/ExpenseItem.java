package org.maks.expensosap.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expense_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ExpenseReport report;
}

