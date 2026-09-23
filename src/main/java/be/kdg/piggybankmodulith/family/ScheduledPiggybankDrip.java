package be.kdg.piggybankmodulith.family;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.domain.OwnerId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ScheduledPiggybankDrip {


    private static final Logger logger = LoggerFactory.getLogger(ScheduledPiggybankDrip.class);

    private final List<Piggybank> piggybanks = List.of(Piggybank.createForOwner(OwnerId.BOBBY));
    private final List<SavingsPromise> savingsPromises = List.of(SavingsPromise.promise(be.kdg.piggybankmodulith.grandparents.domain.OwnerId.of(OwnerId.BOBBY.uuid()), BigDecimal.valueOf(50L)));


    @Scheduled(fixedRate = 10_000)
    void topUp() {
        Piggybank piggybank = piggybanks.stream().filter(pb -> pb.getOwner().equals(OwnerId.BOBBY)).findFirst().orElseThrow();
        piggybank.deposit(BigDecimal.TEN);
        handleEvents(piggybank.getDomainEvents());
        piggybank.clearDomainEvents();
    }

    private void handleEvents(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            switch (event) {
                case MoneyDepositedEvent moneyDepositedEvent -> handleMoneyDeposited(moneyDepositedEvent);

                case MoneyWithdrawalEvent moneyWithdrawalEvent -> handleMoneyWithdrawal(moneyWithdrawalEvent);

                case PromiseLimitBoundaryReachedEvent promiseLimitBoundaryReachedEvent ->
                        handlePromiseLimitBoundaryReached(promiseLimitBoundaryReachedEvent);

                default -> logger.warn("Unhandled domain event: {}", event.getClass().getSimpleName());
            }
        }
    }

    private void handleMoneyDeposited(MoneyDepositedEvent moneyDepositedEvent) {
        SavingsPromise promise = savingsPromises.stream().filter(sp -> sp.getOwner().uuid().equals(OwnerId.BOBBY.uuid())).findFirst().orElseThrow();
        promise.increaseBalance(moneyDepositedEvent.amount());
        handleEvents(promise.getDomainEvents());
        promise.clearDomainEvents();
    }

    private void handleMoneyWithdrawal(MoneyWithdrawalEvent moneyWithdrawalEvent) {
        SavingsPromise promise =  savingsPromises.stream().filter(sp -> sp.getOwner().uuid().equals(OwnerId.BOBBY.uuid())).findFirst().orElseThrow();
        promise.decreaseBalance(moneyWithdrawalEvent.amount());
        handleEvents(promise.getDomainEvents());
        promise.clearDomainEvents();
    }

    private void handlePromiseLimitBoundaryReached(PromiseLimitBoundaryReachedEvent event) {
        logger.error("Piggybank promise limit boundary reached!!!! Let's GO SHOPPING with {}!!!!!", event.owner());
        System.exit(0);
    }

}
