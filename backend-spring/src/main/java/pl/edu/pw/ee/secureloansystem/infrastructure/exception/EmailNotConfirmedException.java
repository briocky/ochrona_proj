package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailNotConfirmedException extends RuntimeException {

  private EmailNotConfirmedException(String message) {
    super(message);
  }

  public static EmailNotConfirmedException of(String message) {
    return new EmailNotConfirmedException(message);
  }
}
