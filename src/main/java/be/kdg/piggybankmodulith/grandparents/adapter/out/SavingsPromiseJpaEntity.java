package be.kdg.piggybankmodulith.grandparents.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "kdg_grandparents", name = "savings_promise")
class SavingsPromiseJpaEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private UUID grandchild;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "promise_boundary", nullable = false, precision = 19, scale = 2)
    private BigDecimal promiseBoundary;

    @Column(nullable = false)
    private boolean triggered;

    protected SavingsPromiseJpaEntity() {} // for JPA

    SavingsPromiseJpaEntity(UUID uuid, UUID grandchild, BigDecimal balance, BigDecimal promiseBoundary,
                            boolean triggered) {
        this.uuid = uuid;
        this.grandchild = grandchild;
        this.balance = balance;
        this.promiseBoundary = promiseBoundary;
        this.triggered = triggered;
    }

    UUID getUuid() {
        return uuid;
    }

    UUID getGrandchild() {
        return grandchild;
    }

    BigDecimal getBalance() {
        return balance;
    }

    BigDecimal getPromiseBoundary() {
        return promiseBoundary;
    }

    boolean isTriggered() {
        return triggered;
    }
}
