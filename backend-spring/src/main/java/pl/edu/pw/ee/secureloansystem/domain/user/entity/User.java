package pl.edu.pw.ee.secureloansystem.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.pw.ee.secureloansystem.domain.loan.entity.Loan;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
  @SequenceGenerator(name = "users_seq", allocationSize = 1)
  Long id;

  @NotNull
  @Email
  @Column(unique = true)
  String email;

  String password;

  @NotNull
  @Size(min = 1, max = 50)
  String firstName;

  @NotNull
  @Size(min = 1, max = 50)
  String lastName;

  LocalDate birthDate;

  @OneToMany(mappedBy = "borrower")
  Set<Loan> loans = new HashSet<>();

  @Builder.Default
  @Transient
  Set<GrantedAuthority> authorities = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
