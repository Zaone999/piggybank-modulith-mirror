package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Read model: one row per piggybank per month. Unlike the event store, rows are updated.
 */
@Entity
@Table(schema = "kdg_family", name = "monthly_savings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"owner", "month"}))
class MonthlySavingsJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID owner;

    // the first day of the month
    @Column(nullable = false)
    private LocalDate month;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected MonthlySavingsJpaEntity() {} // for JPA

    MonthlySavingsJpaEntity(UUID id, UUID owner, LocalDate month, BigDecimal balance) {
        this.id = id;
        this.owner = owner;
        this.month = month;
        this.balance = balance;
    }

    UUID getOwner() {
        return owner;
    }

    LocalDate getMonth() {
        return month;
    }

    BigDecimal getBalance() {
        return balance;
    }

    void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
