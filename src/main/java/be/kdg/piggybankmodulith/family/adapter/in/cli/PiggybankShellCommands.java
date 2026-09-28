package be.kdg.piggybankmodulith.family.adapter.in.cli;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawUseCase;
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
 *  init --owner 8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296
 *  deposit --owner 8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296 --amount 10
 *  withdraw --owner 8c8c87c1-9b5f-4f73-9fa7-7f9b5c7f2296 --amount 5
 *
 * or run the same commands from a file with scripts/init-and-deposit.sh (or .bat on Windows)
 */


@Component
public class PiggybankShellCommands {

    private final CreatePiggybankUseCase createPiggybankUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;

    public PiggybankShellCommands(
            CreatePiggybankUseCase createPiggybankUseCase,
            DepositUseCase depositUseCase,
            WithdrawUseCase withdrawUseCase) {
        this.createPiggybankUseCase = createPiggybankUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
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

    @Command(name = "withdraw", description = "Withdraw money from the piggybank of an owner")
    public String withdraw(
            @Option(longName = "owner", required = true) UUID ownerId,
            @Option(longName = "amount", required = true) BigDecimal amount) {
        Piggybank piggybank = withdrawUseCase.withdraw(new WithdrawMoneyCommand(ownerId, new Money(amount)));

        return "New balance: " + piggybank.getBalance();
    }
}
