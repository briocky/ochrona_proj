package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

  private UserNotFoundException(String message) {
    super(message);
  }

  public static UserNotFoundException of(String message) {
    return new UserNotFoundException(message);
  }
}
