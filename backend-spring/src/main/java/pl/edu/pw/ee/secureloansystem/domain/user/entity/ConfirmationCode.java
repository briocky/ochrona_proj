package pl.edu.pw.ee.secureloansystem.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity(name = "confirmation_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmationCode {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_codes_seq")
  @SequenceGenerator(name = "confirmation_codes_seq", allocationSize = 1)
  Long id;
  String confirmationCode;
  @OneToOne
  @JoinColumn(name = "user_id")
  User user;
}
