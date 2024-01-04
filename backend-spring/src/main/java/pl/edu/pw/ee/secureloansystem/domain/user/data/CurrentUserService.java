package pl.edu.pw.ee.secureloansystem.domain.user.data;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.AuthUser;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

  public AuthUser getCurrentUser() {
    return Optional
        .ofNullable(
            (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        .orElseThrow(() -> new RuntimeException("Principal could not be found"));
  }
}
