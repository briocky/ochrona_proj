package pl.edu.pw.ee.secureloansystem.domain.loan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanStatus;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Entity(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loans_seq")
  @SequenceGenerator(name = "loans_seq", allocationSize = 1)
  Long id;

  @ManyToOne
  @NotNull
  User borrower;

  @ManyToOne
  @NotNull
  User lender;

  @NotNull
  LocalDate dueDate;

  @NotNull
  LocalDate createdAt;

  LocalDate repaidAt;

  String purpose;

  @NotNull
  @Positive
  Double amount;

  @Enumerated(EnumType.STRING)
  LoanStatus status;
}
