package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankCommand;
import be.kdg.piggybankmodulith.family.port.in.CreatePiggybankUseCase;
import be.kdg.piggybankmodulith.family.port.in.DepositMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.DepositUseCase;
import be.kdg.piggybankmodulith.family.port.in.ListPiggybanksQuery;
import be.kdg.piggybankmodulith.family.port.in.WithdrawMoneyCommand;
import be.kdg.piggybankmodulith.family.port.in.WithdrawUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/piggybanks")
public class PiggybankController {

    private final CreatePiggybankUseCase createPiggybankUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final ListPiggybanksQuery listPiggybanksQuery;

    public PiggybankController(
            CreatePiggybankUseCase createPiggybankUseCase,
            DepositUseCase depositUseCase,
            WithdrawUseCase withdrawUseCase,
            ListPiggybanksQuery listPiggybanksQuery) {
        this.createPiggybankUseCase = createPiggybankUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.listPiggybanksQuery = listPiggybanksQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PiggybankResponse createPiggybank(@RequestBody CreatePiggybankRequest request) {
        CreatePiggybankCommand command = new CreatePiggybankCommand(request.ownerId());
        Piggybank piggybank = createPiggybankUseCase.createPiggybank(command);

        return PiggybankResponse.from(piggybank);
    }

    @PostMapping("/{ownerId}/deposits")
    public PiggybankResponse deposit(
            @PathVariable UUID ownerId,
            @RequestBody DepositRequest request) {
        DepositMoneyCommand command = new DepositMoneyCommand(ownerId, new Money(request.amount()));
        Piggybank piggybank = depositUseCase.deposit(command);

        return PiggybankResponse.from(piggybank);
    }

    @PostMapping("/{ownerId}/withdrawals")
    public PiggybankResponse withdraw(
            @PathVariable UUID ownerId,
            @RequestBody WithdrawRequest request) {
        WithdrawMoneyCommand command = new WithdrawMoneyCommand(ownerId, new Money(request.amount()));
        Piggybank piggybank = withdrawUseCase.withdraw(command);

        return PiggybankResponse.from(piggybank);
    }

    @GetMapping
    public List<PiggybankResponse> listPiggybanks() {
        return listPiggybanksQuery.listPiggybanks().stream()
                .map(PiggybankResponse::from)
                .toList();
    }
}
