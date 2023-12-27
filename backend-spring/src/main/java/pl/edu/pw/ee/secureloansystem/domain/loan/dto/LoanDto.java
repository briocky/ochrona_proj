package pl.edu.pw.ee.secureloansystem.domain.loan.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanDto {

  Long id;
  double amount;
  String lenderEmail;
  String purpose;
  LocalDate dueDate;
  LoanStatus status;
}
