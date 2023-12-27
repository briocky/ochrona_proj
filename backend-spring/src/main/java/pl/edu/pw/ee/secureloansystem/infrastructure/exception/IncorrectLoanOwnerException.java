package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncorrectLoanOwnerException extends RuntimeException {

  private IncorrectLoanOwnerException(String message) {
    super(message);
  }

  public static IncorrectLoanOwnerException of(String message) {
    return new IncorrectLoanOwnerException(message);
  }
}
