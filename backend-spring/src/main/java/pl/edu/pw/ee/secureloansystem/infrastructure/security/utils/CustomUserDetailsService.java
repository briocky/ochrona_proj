package pl.edu.pw.ee.secureloansystem.infrastructure.security.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.data.UserManagementService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {

  final UserManagementService userManagementService;

  @Override
  public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {
    return userManagementService.getUserByEmail(email);
  }
}
