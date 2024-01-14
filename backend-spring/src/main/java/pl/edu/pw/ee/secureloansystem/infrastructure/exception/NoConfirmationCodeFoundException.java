package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoConfirmationCodeFoundException extends RuntimeException {

  private NoConfirmationCodeFoundException(String message) {
    super(message);
  }

  public static NoConfirmationCodeFoundException of(String message) {
    return new NoConfirmationCodeFoundException(message);
  }
}
