package pl.edu.pw.ee.secureloansystem.domain.user.data;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.domain.user.mapper.UserMapper;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.BadPasswordException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.EmailTakenException;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.TokenUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {

  static final Long BRUTE_FORCE_PREVENTION_SLEEP_MILLIS = 500L;

  final UserManagementService userManagementService;
  final PasswordEncoder passwordEncoder;
  final UserMapper userMapper;
  final TokenUtils tokenUtils;

  public LoginResponse loginUser(LoginRequest request) {
    preventBruteForceAttack();

    User userByEmail = userManagementService.getUserByEmail(request.getEmail());

    boolean passwordMatches = passwordEncoder.matches(
      request.getPassword(),
      userByEmail.getPassword()
    );

    if (!passwordMatches) {
      throw BadPasswordException.of("Passwords not match!");
    }

    LoginResponse loginResponse = userMapper.getLoginResponse(userByEmail);
    loginResponse.setAccessToken(tokenUtils.generateAccessToken(userByEmail));
    loginResponse.setRefreshToken(tokenUtils.generateRefreshToken(userByEmail));
    return loginResponse;
  }

  public RegisterResponse registerUser(RegisterRequest request) {
    Optional<User> userOptional = userManagementService.getUserOptionalByEmail(
      request.getEmail()
    );
    if (userOptional.isPresent()) {
      throw EmailTakenException.of(
        String.format("Email %s already taken.", request.getEmail())
      );
    }

    User mappedUser = userMapper.getModel(request);
    mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

    User savedUser = userManagementService.createUser(mappedUser);

    RegisterResponse registerResponse = userMapper.getRegisterResponse(
      savedUser
    );
    registerResponse.setAccessToken(tokenUtils.generateAccessToken(savedUser));
    registerResponse.setRefreshToken(
      tokenUtils.generateRefreshToken(savedUser)
    );
    return registerResponse;
  }

  private void preventBruteForceAttack() {
    try {
      Thread.sleep(BRUTE_FORCE_PREVENTION_SLEEP_MILLIS);
    } catch (InterruptedException e) {
      throw new RuntimeException(
        "An error occurred while preventing brute force attack.",
        e
      );
    }
  }
}
