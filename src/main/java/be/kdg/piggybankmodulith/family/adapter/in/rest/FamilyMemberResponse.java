package be.kdg.piggybankmodulith.family.adapter.in.rest;

import be.kdg.piggybankmodulith.family.domain.FamilyMember;

import java.util.UUID;

public record FamilyMemberResponse(UUID familyMemberId, String firstName, String lastName) {

    public static FamilyMemberResponse from(FamilyMember familyMember) {
        return new FamilyMemberResponse(
                familyMember.getFamilyMemberId().uuid(),
                familyMember.getFirstName(),
                familyMember.getLastName());
    }
}
