package pl.edu.pw.ee.secureloansystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownLoanRequestAction extends RuntimeException {

  private UnknownLoanRequestAction(String message) {
    super(message);
  }

  public static UnknownLoanRequestAction of(String message) {
    return new UnknownLoanRequestAction(message);
  }
}
