package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

  @NotNull
  @Email
  String email;

  @NotNull
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{5,50}$",
    message = "Password must contain at last: 1 lowercase letter, 1 uppercase letter, 1 digit, 1 special character"
  )
  String password;
}
