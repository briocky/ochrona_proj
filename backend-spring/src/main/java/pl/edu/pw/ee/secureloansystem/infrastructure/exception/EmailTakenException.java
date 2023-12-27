package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailTakenException extends RuntimeException {

  private EmailTakenException(String message) {
    super(message);
  }

  public static EmailTakenException of(String message) {
    return new EmailTakenException(message);
  }
}
