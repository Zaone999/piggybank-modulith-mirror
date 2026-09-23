package be.kdg.piggybankmodulith.family.adapter.in;

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
    public ResponseEntity<PiggybankDto> putMoneyIn(@RequestBody DepositRequest request) throws FamilyMemberNotFoundException {
        DepositMoneyCommand command = new DepositMoneyCommand(request.owner(), request.amount());
        Piggybank piggybank = this.depositMoneyUseCase.deposit(command);

        return ResponseEntity.ok(new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance()));
    }

    @DeleteMapping("/piggybanks")
    public ResponseEntity<PiggybankDto> takeMoneyOut(@RequestBody WithdrawalRequest request) throws FamilyMemberNotFoundException, PiggyBankNotFoundException {
        WithdrawMoneyCommand command = new WithdrawMoneyCommand(request.owner(), request.amount());
        Piggybank piggybank = withdrawMoneyOutUseCase.withdraw(command);

        return ResponseEntity.ok(new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance()));
    }

    @GetMapping("/piggybanks")
    public ResponseEntity<List<PiggybankDto>> findAll() {
        List<PiggybankDto> piggybankDtos = this.findAllPiggybanksPort.findAll().stream()
                .map(piggybank -> new PiggybankDto(piggybank.getOwner().uuid(), piggybank.getBalance()))
                .toList();

        return ResponseEntity.ok(piggybankDtos);
    }
}
