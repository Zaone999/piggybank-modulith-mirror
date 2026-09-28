package be.kdg.piggybankmodulith.grandparents.domain.exceptions;

public class SavingsPromiseAlreadyExistsException extends RuntimeException {

    public SavingsPromiseAlreadyExistsException(String message) {
        super(message);
    }
}
