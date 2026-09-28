package be.kdg.piggybankmodulith.family.adapter.in.cli;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * TESTING THIS:
 *
 * run in a terminal using ./mvnw spring-boot:run so that the terminal show us (and the mvn thread is not forked)
 *
 *  init 8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296
 *  deposit --owner 8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296 --amount 10
 */


@Component
public class PiggybankShellCommands {

    private final DepositUseCase depositUseCase;
    private final CreatePiggybankUseCase createPiggybankUseCase;

    public PiggybankShellCommands(DepositUseCase depositUseCase, CreatePiggybankUseCase createPiggybankUseCase) {
        this.depositUseCase = depositUseCase;
        this.createPiggybankUseCase = createPiggybankUseCase;
    }

    @Command(name = "init", description = "Init piggybank for an owner")
    public String init(
            @Option(longName = "owner", required = true) UUID ownerId) {
        Piggybank piggybank = createPiggybankUseCase.createPiggybank(new CreatePiggybankCommand(ownerId));

        return "New balance: " + piggybank.getBalance();
    }

    @Command(name = "deposit", description = "Deposit money into the piggybank of an owner")
    public String deposit(
            @Option(longName = "owner", required = true) UUID ownerId,
            @Option(longName = "amount", required = true) BigDecimal amount) {
        Piggybank piggybank = depositUseCase.deposit(new DepositMoneyCommand(ownerId, new Money(amount)));

        return "New balance: " + piggybank.getBalance();
    }
}
