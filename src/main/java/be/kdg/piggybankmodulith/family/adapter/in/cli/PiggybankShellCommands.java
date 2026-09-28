package be.kdg.piggybankmodulith.family.adapter.in.cli;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import be.kdg.piggybankmodulith.family.port.in.BalanceForMonth;
import be.kdg.piggybankmodulith.family.port.in.ListPiggybanksQuery;
import be.kdg.piggybankmodulith.family.port.in.PiggybankBalanceQuery;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthCommand;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthUseCase;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotUseCase;
import be.kdg.piggybankmodulith.family.port.in.TakeSnapshotsCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawUseCase;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TESTING THIS:
 *
 * run in a terminal using ./mvnw spring-boot:run so that the terminal show us (and the mvn thread is not forked)
 *
 *  born --member 89a7e903-4c29-4fba-ac52-253292126794 --first-name Emma --last-name Peeters
 *  init --owner 89a7e903-4c29-4fba-ac52-253292126794
 *  deposit --owner 89a7e903-4c29-4fba-ac52-253292126794 --amount 10
 *  withdraw --owner 89a7e903-4c29-4fba-ac52-253292126794 --amount 5
 *  piggybanks
 *  balance --owner <owner>
 *  balance --owner <owner> --month 2026-09
 *  snapshot
 *  snapshot --date 2026-09-01T00:00
 *  exit
 *
 * or run the same commands from a file with scripts/init-and-deposit.sh (or .bat on Windows)
 */


@Component
public class PiggybankShellCommands {

    private final RegisterBirthUseCase registerBirthUseCase;
    private final CreatePiggybankUseCase createPiggybankUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final ListPiggybanksQuery listPiggybanksQuery;
    private final PiggybankBalanceQuery piggybankBalanceQuery;
    private final TakeSnapshotUseCase takeSnapshotUseCase;

    public PiggybankShellCommands(
            RegisterBirthUseCase registerBirthUseCase,
            CreatePiggybankUseCase createPiggybankUseCase,
            DepositUseCase depositUseCase,
            WithdrawUseCase withdrawUseCase,
            ListPiggybanksQuery listPiggybanksQuery,
            PiggybankBalanceQuery piggybankBalanceQuery,
            TakeSnapshotUseCase takeSnapshotUseCase) {
        this.registerBirthUseCase = registerBirthUseCase;
        this.createPiggybankUseCase = createPiggybankUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.listPiggybanksQuery = listPiggybanksQuery;
        this.piggybankBalanceQuery = piggybankBalanceQuery;
        this.takeSnapshotUseCase = takeSnapshotUseCase;
    }

    @Command(name = "born", description = "Register the birth of a family member")
    public String born(
            @Option(longName = "member", required = true) UUID familyMemberId,
            @Option(longName = "first-name", required = true) String firstName,
            @Option(longName = "last-name", required = true) String lastName) {
        FamilyMember familyMember = registerBirthUseCase.registerBirth(
                new RegisterBirthCommand(familyMemberId, firstName, lastName));

        return "Born: " + familyMember.getFirstName() + " " + familyMember.getLastName();
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

    @Command(name = "piggybanks", description = "List all piggybanks with their owner and balance")
    public String piggybanks() {
        return listPiggybanksQuery.listPiggybanks().stream()
                .map(piggybank -> "Owner " + piggybank.getOwner().uuid() + ": balance " + piggybank.getBalance())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Command(name = "balance", description = "Replay the events of a piggybank: the current balance, or the balance for one month")
    public String balance(
            @Option(longName = "owner", required = true) UUID ownerId,
            @Option(longName = "month") String month) {
        if (month == null) {
            return "Current balance: " + piggybankBalanceQuery.currentBalance(ownerId);
        }
        YearMonth yearMonth = YearMonth.parse(month);
        return "Balance for " + yearMonth + ": "
                + piggybankBalanceQuery.balanceForMonth(new BalanceForMonth(ownerId, yearMonth));
    }

    // what the monthly scheduler does, by hand: without a date, a snapshot of everything up to now
    @Command(name = "snapshot", description = "Take a snapshot of every piggybank, covering the events before the date")
    public String snapshot(@Option(longName = "date") String date) {
        LocalDateTime snapshotDate = date == null ? LocalDateTime.now() : LocalDateTime.parse(date);
        takeSnapshotUseCase.takeSnapshots(new TakeSnapshotsCommand(snapshotDate));

        return "Snapshots taken on " + snapshotDate;
    }

    @Command(name = "exit", description = "Stop the application")
    public void exit() {
        System.exit(0);
    }
}
