package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "piggybank", schema = "kdg_family")
public class PiggybankJpaEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private UUID owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public PiggybankJpaEntity() {
        this.uuid = UUID.randomUUID(); // assign logical ID on creation
    }

    public PiggybankJpaEntity(UUID owner, BigDecimal balance) {
        this();
        this.owner = owner;
        this.balance = balance;
    }

    public PiggybankJpaEntity(UUID uuid, UUID owner, BigDecimal balance) {
        this.uuid = uuid;
        this.owner = owner;
        this.balance = balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
