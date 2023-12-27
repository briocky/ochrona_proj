package pl.edu.pw.ee.secureloansystem.application;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.ee.secureloansystem.domain.user.data.AuthService;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterResponse;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.CookieService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
class AuthController {

  final AuthService authService;
  final CookieService cookieService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> loginUser(
    @Valid @RequestBody LoginRequest request,
    HttpServletResponse response
  ) {
    LoginResponse loginResponse = authService.loginUser(request);
    cookieService.addRefreshTokenCookie(
      response,
      loginResponse.getRefreshToken()
    );
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> registerUser(
    @Valid @RequestBody RegisterRequest request,
    HttpServletResponse response
  ) {
    RegisterResponse registerResponse = authService.registerUser(request);
    cookieService.addRefreshTokenCookie(
      response,
      registerResponse.getRefreshToken()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
  }
}
