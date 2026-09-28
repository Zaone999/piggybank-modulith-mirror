package be.kdg.piggybankmodulith.family.adapter.out;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(schema = "kdg_family", name = "piggybank")
class PiggybankJpaEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private UUID owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected PiggybankJpaEntity() {} // for JPA

    PiggybankJpaEntity(UUID uuid, UUID owner, BigDecimal balance) {
        this.uuid = uuid;
        this.owner = owner;
        this.balance = balance;
    }

    UUID getUuid() {
        return uuid;
    }

    UUID getOwner() {
        return owner;
    }

    BigDecimal getBalance() {
        return balance;
    }
}
