package be.kdg.piggybankmodulith.family.adapter.in;

import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankAlreadyExistsException;
import be.kdg.piggybankmodulith.family.domain.exceptions.PiggyBankNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = PiggybankController.class)
class PiggybankExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PiggyBankNotFoundException.class)
    ProblemDetail handleNotFound(PiggyBankNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(PiggyBankAlreadyExistsException.class)
    ProblemDetail handleAlreadyExists(PiggyBankAlreadyExistsException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleInvalidInput(IllegalArgumentException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
