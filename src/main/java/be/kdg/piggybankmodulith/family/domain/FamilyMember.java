package be.kdg.piggybankmodulith.family.domain;

import be.kdg.piggybankmodulith.common.events.DomainEvent;
import be.kdg.piggybankmodulith.common.events.FamilyMemberBornEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FamilyMember {

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyMember.class);

    private final FamilyMemberId familyMemberId;
    private final String firstName;
    private final String lastName;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private FamilyMember(FamilyMemberId familyMemberId, String firstName, String lastName) {
        Objects.requireNonNull(familyMemberId, "Family member ID must not be null");
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name must not be blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name must not be blank");
        }

        this.familyMemberId = familyMemberId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static FamilyMember born(FamilyMemberId familyMemberId, String firstName, String lastName) {
        FamilyMember familyMember = new FamilyMember(familyMemberId, firstName, lastName);
        familyMember.domainEvents.add(new FamilyMemberBornEvent(familyMemberId.uuid(), firstName, lastName));
        LOGGER.info("{} {} is born", firstName, lastName);
        return familyMember;
    }

    public static FamilyMember restore(FamilyMemberId familyMemberId, String firstName, String lastName) {
        return new FamilyMember(familyMemberId, firstName, lastName);
    }

    public FamilyMemberId getFamilyMemberId() {
        return familyMemberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "familyMemberId=" + familyMemberId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
