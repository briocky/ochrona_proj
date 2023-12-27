package pl.edu.pw.ee.secureloansystem.domain.user.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class SecureUser implements UserDetails {

  @NotNull
  @Email
  @Column(unique = true)
  String email;

  String password;

  @Builder.Default
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
    return false;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
