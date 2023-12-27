package pl.edu.pw.ee.secureloansystem.infrastructure.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

  @Value("${security.cookie.domain}")
  private String domain;

  @Value("${security.cookie.max-age}")
  private int maxAge;

  public void addRefreshTokenCookie(
    HttpServletResponse response,
    String refreshToken
  ) {
    response.addCookie(makeRefreshTokenCookie(refreshToken));
  }

  private Cookie makeRefreshTokenCookie(String value) {
    final String cookieName = "refreshToken";
    final String cookieUrl = "/api/auth/refreshToken";
    Cookie cookie = new Cookie(cookieName, value);
    cookie.setHttpOnly(true);
    cookie.setDomain(domain);
    cookie.setPath(cookieUrl);
    cookie.setMaxAge(maxAge);
    return cookie;
  }
}
