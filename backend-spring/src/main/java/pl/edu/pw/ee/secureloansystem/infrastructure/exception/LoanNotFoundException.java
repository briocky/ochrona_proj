package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException {

  private LoanNotFoundException(String message) {
    super(message);
  }

  public static LoanNotFoundException of(String message) {
    return new LoanNotFoundException(message);
  }
}
