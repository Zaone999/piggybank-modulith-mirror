package be.kdg.piggybankmodulith.grandparents.adapter.out;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "savings_promise", schema = "kdg_grandparents")
public class SavingsPromiseJpaEntity {

    @Id
    private UUID owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal promise;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public SavingsPromiseJpaEntity() {
    }

    public SavingsPromiseJpaEntity(UUID owner, BigDecimal balance, BigDecimal promise) {
        this.owner = owner;
        this.balance = balance;
        this.promise = promise;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getPromise() {
        return promise;
    }

}