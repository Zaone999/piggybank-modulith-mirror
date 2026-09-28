package be.kdg.piggybankmodulith.family.port.out;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;
import be.kdg.piggybankmodulith.family.domain.FamilyMemberId;

import java.util.Optional;

public interface FamilyMemberLoadPort {

    Optional<FamilyMember> loadById(FamilyMemberId familyMemberId);
}
