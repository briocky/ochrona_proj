package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadConfirmationCodeException extends RuntimeException {

  private BadConfirmationCodeException(String message) {
    super(message);
  }

  public static BadConfirmationCodeException of(String message) {
    return new BadConfirmationCodeException(message);
  }
}
