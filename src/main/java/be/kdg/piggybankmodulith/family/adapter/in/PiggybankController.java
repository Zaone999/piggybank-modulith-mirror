package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.common.Money;
import be.kdg.piggybankmodulith.family.adapter.in.request.WithdrawalRequest;
import be.kdg.piggybankmodulith.family.adapter.in.response.PiggybankDto;
import be.kdg.piggybankmodulith.family.adapter.in.request.DepositRequest;
import be.kdg.piggybankmodulith.family.domain.Piggybank;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberNotFoundException;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import be.kdg.piggybankmodulith.family.port.in.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PiggybankController {

    private final DepositMoneyUseCase depositMoneyUseCase;
    private final WithdrawMoneyOutUseCase withdrawMoneyOutUseCase;
    private final FindAllPiggybanksPort findAllPiggybanksPort;

    public PiggybankController(DepositMoneyUseCase depositMoneyUseCase, WithdrawMoneyOutUseCase withdrawMoneyOutUseCase, FindAllPiggybanksPort findAllPiggybanksPort) {
        this.depositMoneyUseCase = depositMoneyUseCase;
        this.withdrawMoneyOutUseCase = withdrawMoneyOutUseCase;
        this.findAllPiggybanksPort = findAllPiggybanksPort;
    }

    @PostMapping("/piggybanks")
    public ResponseEntity<PiggybankDto> deposit(@RequestBody DepositRequest request) throws FamilyMemberNotFoundException {
        DepositMoneyCommand command = new DepositMoneyCommand(request.owner(), Money.valueOf(request.amount()));
        Piggybank piggybank = this.depositMoneyUseCase.deposit(command);

        return ResponseEntity.ok(new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance().getValue()));
    }

    @DeleteMapping("/piggybanks")
    public ResponseEntity<PiggybankDto> withdraw(@RequestBody WithdrawalRequest request) throws FamilyMemberNotFoundException, PiggyBankNotFoundException {
        WithdrawMoneyCommand command = new WithdrawMoneyCommand(request.owner(), Money.valueOf(request.amount()));
        Piggybank piggybank = withdrawMoneyOutUseCase.withdraw(command);

        return ResponseEntity.ok(new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance().getValue()));
    }

    @GetMapping("/piggybanks")
    public ResponseEntity<List<PiggybankDto>> findAll() {
        List<PiggybankDto> piggybankDtos = this.findAllPiggybanksPort.findAll().stream()
                .map(piggybank -> new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance().getValue()))
                .toList();

        return ResponseEntity.ok(piggybankDtos);
    }
}
