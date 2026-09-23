package be.kdg.piggybankmodulith.grandparents.adapter.out;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "savings_projection", schema = "kdg_grandparents")
public class SavingsProjectionJpaEntity {

    @Id
    private UUID owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public SavingsProjectionJpaEntity() {
    }

    public SavingsProjectionJpaEntity(UUID owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}