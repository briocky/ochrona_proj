package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadPasswordException extends RuntimeException {

  private BadPasswordException(String message) {
    super(message);
  }

  public static BadPasswordException of(String message) {
    return new BadPasswordException(message);
  }
}
