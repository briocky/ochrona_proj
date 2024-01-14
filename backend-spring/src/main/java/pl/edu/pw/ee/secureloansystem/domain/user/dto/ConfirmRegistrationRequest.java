package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmRegistrationRequest {

  @Email
  @NotNull
  String email;
  @NotNull
  String code;
}
