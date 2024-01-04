package pl.edu.pw.ee.secureloansystem.domain.user.data;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RefreshTokenResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.TokenExpiredException;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.TokenUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenService {

  final TokenUtils tokenUtils;
  final UserManagementService userManagementService;

  public RefreshTokenResponse refreshToken(String refreshToken) {
    String email = tokenUtils.extractEmail(refreshToken);
    User userByEmail = userManagementService.getUserByEmail(email);
    return RefreshTokenResponse.builder()
        .accessToken(tokenUtils.generateAccessToken(userByEmail))
        .refreshToken(tokenUtils.generateRefreshToken(userByEmail))
        .build();
  }
}
