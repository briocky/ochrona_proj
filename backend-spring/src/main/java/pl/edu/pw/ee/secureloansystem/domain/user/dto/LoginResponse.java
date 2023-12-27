package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class LoginResponse {

  Long id;
  String firstName;
  String lastName;
  String email;
  String accessToken;

  @JsonIgnore
  String refreshToken;
}
