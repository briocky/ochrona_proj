package pl.edu.pw.ee.secureloansystem.domain.loan.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanRequest {

  @Positive
  double amount;

  @Email
  @NotNull
  String lenderEmail;

  String purpose;

  @NotNull
  LocalDate dueDate;
}
