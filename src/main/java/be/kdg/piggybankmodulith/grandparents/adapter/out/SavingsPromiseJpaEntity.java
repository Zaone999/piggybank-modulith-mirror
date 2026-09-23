package be.kdg.piggybankmodulith.grandparents.adapter.out;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "savings_promise", schema = "kdg_grandparents")
public class SavingsPromiseJpaEntity {

    @Id
    private UUID grandchild;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal promise;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public SavingsPromiseJpaEntity() {
    }

    public SavingsPromiseJpaEntity(UUID grandchild, BigDecimal balance, BigDecimal promise) {
        this.grandchild = grandchild;
        this.balance = balance;
        this.promise = promise;
    }

    public UUID getGrandchild() {
        return grandchild;
    }

    public void setGrandchild(UUID grandchild) {
        this.grandchild = grandchild;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getPromise() {
        return promise;
    }

}