package be.kdg.piggybankmodulith.family.port.in;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;

public interface RegisterBirthUseCase {

    FamilyMember registerBirth(RegisterBirthCommand command);
}
