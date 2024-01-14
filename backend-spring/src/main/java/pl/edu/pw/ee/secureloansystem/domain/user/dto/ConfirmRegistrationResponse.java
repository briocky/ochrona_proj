package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ConfirmRegistrationResponse {
  String message;
  String accessToken;
  AuthUser user;

  @JsonIgnore
  String refreshToken;
}
