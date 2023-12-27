package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.edu.pw.ee.secureloansystem.infrastructure.validator.ConfirmPassword;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@ConfirmPassword
public class RegisterRequest {

  @NotNull
  @Size(min = 1, max = 50)
  String firstName;

  @NotNull
  @Size(min = 1, max = 50)
  String lastName;

  @NotNull
  @Email
  String email;

  @NotNull
  @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{5,50}$",
    message = "Password must contain at last: 1 lowercase letter, 1 uppercase letter, 1 digit, 1 special character"
  )
  String password;

  String confirmPassword;

  @NotNull
  LocalDate birthDate;
}
