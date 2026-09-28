package be.kdg.piggybankmodulith.family.core;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.domain.FamilyMemberId;
import be.kdg.piggybankmodulith.family.domain.exceptions.FamilyMemberAlreadyExistsException;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthCommand;
import be.kdg.piggybankmodulith.family.port.in.RegisterBirthUseCase;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberLoadPort;
import be.kdg.piggybankmodulith.family.port.out.FamilyMemberUpdatePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
class DefaultRegisterBirthUseCase implements RegisterBirthUseCase {

    private final FamilyMemberLoadPort familyMemberLoadPort;
    private final List<FamilyMemberUpdatePort> familyMemberUpdatePorts;

    DefaultRegisterBirthUseCase(
            FamilyMemberLoadPort familyMemberLoadPort,
            List<FamilyMemberUpdatePort> familyMemberUpdatePorts) {
        this.familyMemberLoadPort = familyMemberLoadPort;
        this.familyMemberUpdatePorts = List.copyOf(familyMemberUpdatePorts);
    }

    @Override
    @Transactional
    public FamilyMember registerBirth(RegisterBirthCommand command) {
        FamilyMemberId familyMemberId = FamilyMemberId.of(command.familyMemberId());

        if (familyMemberLoadPort.loadById(familyMemberId).isPresent()) {
            throw new FamilyMemberAlreadyExistsException(
                    "Family member already exists with id " + command.familyMemberId());
        }

        FamilyMember familyMember = FamilyMember.born(familyMemberId, command.firstName(), command.lastName());

        familyMemberUpdatePorts.forEach(port -> port.update(familyMember));

        return familyMember;
    }
}
