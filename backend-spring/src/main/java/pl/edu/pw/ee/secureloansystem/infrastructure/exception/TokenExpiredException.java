package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenExpiredException extends RuntimeException {
  private TokenExpiredException(String message) {
    super(message);
  }

  public static TokenExpiredException of(String message) {
    return new TokenExpiredException(message);
  }
}
