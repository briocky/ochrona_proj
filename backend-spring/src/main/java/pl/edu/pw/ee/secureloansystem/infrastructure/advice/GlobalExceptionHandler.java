package pl.edu.pw.ee.secureloansystem.infrastructure.advice;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BadPasswordException.class)
  public ResponseEntity<String> handlePasswordsNotMatchExceptions(BadPasswordException ex) {
    log.error("Passwords not match exception: ", ex);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Either email or password is invalid");
  }

  @ExceptionHandler(EmailTakenException.class)
  public ResponseEntity<String> handleEmailTakenException(EmailTakenException ex) {
    log.error("Email taken exception: ", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already taken");
  }

  @ExceptionHandler(LoanNotFoundException.class)
  public ResponseEntity<String> handleLoanNotFoundException(LoanNotFoundException ex) {
    log.error("Loan not found exception: ", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found");
  }

  @ExceptionHandler(UnknownLoanRequestAction.class)
  public ResponseEntity<String> handleUnknownLoanRequestActionException(
      UnknownLoanRequestAction ex) {
    log.error("Unknown loan action exception: ", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown loan request action");
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    log.error("User not found exception: ", ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
  }

  @ExceptionHandler(IncorrectLoanOwnerException.class)
  public ResponseEntity<String> handleIncorrectLoanOwnerException(IncorrectLoanOwnerException ex) {
    log.error("Incorrect loan owner exception: ", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect loan owner");
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
    log.error("Token expired exception: ", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    log.error("Method argument validation exception: ", ex);
    BindingResult bindingResult = ex.getBindingResult();
    return handleBindingResultErrors(bindingResult);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<String> handleBindExceptions(BindException ex) {
    log.error("Validation binding exception: ", ex);
    BindingResult bindingResult = ex.getBindingResult();
    return handleBindingResultErrors(bindingResult);
  }

  private ResponseEntity<String> handleBindingResultErrors(BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      List<FieldError> errors = bindingResult.getFieldErrors();
      StringBuilder errorMessage = new StringBuilder("Validation errors: ");
      for (FieldError error : errors) {
        errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
            .append("; ");
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }
    return ResponseEntity.ok("Success");
  }
}
