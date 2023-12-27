package pl.edu.pw.ee.secureloansystem.domain.debt.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.AuthUser;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtDto {

  Long id;
  AuthUser user;
  double totalAmount;
  boolean hasOverdue;
}
