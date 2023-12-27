package pl.edu.pw.ee.secureloansystem.domain.debt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Entity(name = "debts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Debt {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debts_seq")
  @SequenceGenerator(name = "debts_seq", allocationSize = 1)
  Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  @NotNull
  User user;

  @Column(nullable = false)
  double totalAmount;

  @Column(nullable = false)
  boolean hasOverdue;
}
