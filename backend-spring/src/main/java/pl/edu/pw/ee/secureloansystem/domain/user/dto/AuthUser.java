package pl.edu.pw.ee.secureloansystem.domain.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class AuthUser {

  Long id;
  String email;
  String firstName;
  String lastName;
}
