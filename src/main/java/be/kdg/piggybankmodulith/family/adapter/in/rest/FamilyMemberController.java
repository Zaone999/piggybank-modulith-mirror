package be.kdg.piggybankmodulith.family.adapter.in.rest;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthCommand;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/family-members")
public class FamilyMemberController {

    private final RegisterBirthUseCase registerBirthUseCase;

    public FamilyMemberController(RegisterBirthUseCase registerBirthUseCase) {
        this.registerBirthUseCase = registerBirthUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FamilyMemberResponse born(@RequestBody BornRequest request) {
        RegisterBirthCommand command = new RegisterBirthCommand(
                request.familyMemberId(), request.firstName(), request.lastName());
        FamilyMember familyMember = registerBirthUseCase.registerBirth(command);

        return FamilyMemberResponse.from(familyMember);
    }
}
