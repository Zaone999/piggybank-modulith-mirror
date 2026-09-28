package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "kdg_family", name = "piggybank_snapshot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"piggybank_id", "snapshot_date"}))
class PiggybankSnapshotJpaEntity {

    @Id
    @Column(name = "snapshot_id")
    private UUID snapshotId;

    @Column(name = "piggybank_id", nullable = false)
    private UUID piggybankId;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDateTime snapshotDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected PiggybankSnapshotJpaEntity() {} // for JPA

    PiggybankSnapshotJpaEntity(UUID snapshotId, UUID piggybankId, LocalDateTime snapshotDate, BigDecimal balance) {
        this.snapshotId = snapshotId;
        this.piggybankId = piggybankId;
        this.snapshotDate = snapshotDate;
        this.balance = balance;
    }

    UUID getPiggybankId() {
        return piggybankId;
    }

    LocalDateTime getSnapshotDate() {
        return snapshotDate;
    }

    BigDecimal getBalance() {
        return balance;
    }
}
