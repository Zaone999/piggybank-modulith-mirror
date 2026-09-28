package be.kdg.piggybankmodulith.family.domain.exceptions;

public class FamilyMemberAlreadyExistsException extends RuntimeException {

    public FamilyMemberAlreadyExistsException(String message) {
        super(message);
    }
}
