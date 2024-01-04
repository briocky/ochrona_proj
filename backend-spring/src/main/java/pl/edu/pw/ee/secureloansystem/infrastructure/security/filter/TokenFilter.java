package pl.edu.pw.ee.secureloansystem.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.domain.user.mapper.UserMapper;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.TokenExpiredException;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.TokenUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenFilter extends OncePerRequestFilter {

  static final String AUTH_HEADER_PREFIX = "Bearer ";
  static final String REFRESH_TOKEN_URI = "/api/auth/refreshToken";
  static final String TOKEN_EXPIRED_MSG = "Token expired";
  static final String REFRESH_TOKEN_EXPIRED_MSG = "Refresh token expired";

  final TokenUtils tokenUtils;
  final UserDetailsService userDetailsService;
  final UserMapper userMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken = getRefreshCookie(request);

    if (authHeader != null && authHeader.startsWith(AUTH_HEADER_PREFIX)) {
      final String token = authHeader.substring(AUTH_HEADER_PREFIX.length());
      checkTokenExpiration(token, TOKEN_EXPIRED_MSG);
      authenticateUserFromToken(token);
    } else if (refreshToken != null && request.getRequestURI().equals(REFRESH_TOKEN_URI)) {
      checkTokenExpiration(refreshToken, REFRESH_TOKEN_EXPIRED_MSG);
      authenticateUserFromToken(refreshToken);
    } else {
      log.debug("Token not found");
    }
    filterChain.doFilter(request, response);
  }

  private void authenticateUserFromToken(String token) {
    final String email = tokenUtils.extractEmail(token);
    final User loadedUser = (User) userDetailsService.loadUserByUsername(email);
    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        userMapper.getAuthUser(loadedUser), null, loadedUser.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    log.debug("Token verified");
  }

  private String getRefreshCookie(HttpServletRequest request) {
    final String refreshTokenCookieName = "refreshToken";
    Cookie[] cookies = request.getCookies();
    String refreshTokenValue = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (refreshTokenCookieName.equals(cookie.getName())) {
          refreshTokenValue = cookie.getValue();
          break;
        }
      }
    }
    return refreshTokenValue;
  }

  private void checkTokenExpiration(String token, String message) {
    if (tokenUtils.isTokenExpired(token)) {
      log.debug(message);
      throw TokenExpiredException.of(message);
    }
  }
}
