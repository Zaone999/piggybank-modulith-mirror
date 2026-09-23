package be.kdg.piggybankmodulith.choreography;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.MoneyDepositedEvent;
import be.kdg.piggybankmodulith.common.events.MoneyWithdrawalEvent;
import be.kdg.piggybankmodulith.common.events.PromiseLimitBoundaryReachedEvent;
import be.kdg.piggybankmodulith.family.domain.GrandchildId;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.grandparents.domain.SavingsPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledPiggybankDrip {


    private static final Logger logger = LoggerFactory.getLogger(ScheduledPiggybankDrip.class);

    private final List<Piggybank> piggybanks = List.of(Piggybank.createForOwner(GrandchildId.BOBBY));
    private final List<SavingsPromise> savingsPromises = List.of(SavingsPromise.promise(GrandchildId.of(GrandchildId.BOBBY.uuid()), Money.FIFTY));


    @Scheduled(fixedRate = 10_000)
    void topUp() {
        Piggybank piggybank = piggybanks.stream().filter(pb -> pb.getOwner().equals(GrandchildId.BOBBY)).findFirst().orElseThrow();
        piggybank.deposit(Money.TEN);
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
        SavingsPromise promise = savingsPromises.stream().filter(sp -> sp.getGrandchild().uuid().equals(GrandchildId.BOBBY.uuid())).findFirst().orElseThrow();
        promise.increaseBalance(moneyDepositedEvent.amount());
        handleEvents(promise.getDomainEvents());
        promise.clearDomainEvents();
    }

    private void handleMoneyWithdrawal(MoneyWithdrawalEvent moneyWithdrawalEvent) {
        SavingsPromise promise =  savingsPromises.stream().filter(sp -> sp.getGrandchild().uuid().equals(GrandchildId.BOBBY.uuid())).findFirst().orElseThrow();
        promise.decreaseBalance(moneyWithdrawalEvent.amount());
        handleEvents(promise.getDomainEvents());
        promise.clearDomainEvents();
    }

    private void handlePromiseLimitBoundaryReached(PromiseLimitBoundaryReachedEvent event) {
        logger.error("Piggybank promise limit boundary reached!!!! Let's GO SHOPPING with {}!!!!!", event.grandchild());
        System.exit(0);
    }

}
