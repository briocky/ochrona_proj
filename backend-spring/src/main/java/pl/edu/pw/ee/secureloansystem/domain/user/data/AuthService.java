package pl.edu.pw.ee.secureloansystem.domain.user.data;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.ConfirmRegistrationRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.ConfirmRegistrationResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.ConfirmationCode;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.domain.user.mapper.UserMapper;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.BadConfirmationCodeException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.BadPasswordException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.EmailNotConfirmedException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.EmailTakenException;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.TokenUtils;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {

  static final Long BRUTE_FORCE_PREVENTION_SLEEP_MILLIS = 500L;

  final UserManagementService userManagementService;
  final PasswordEncoder passwordEncoder;
  final UserMapper userMapper;
  final TokenUtils tokenUtils;
  final EmailService emailService;
  final ConfirmationCodeService confirmationCodeService;

  public LoginResponse loginUser(LoginRequest request) {
    preventBruteForceAttack();

    User userByEmail = userManagementService.getUserByEmail(request.getEmail());

    checkEmailConfirmed(userByEmail);
    checkPassword(request.getPassword(), userByEmail.getPassword());

    LoginResponse loginResponse = userMapper.getLoginResponse(userByEmail);
    loginResponse.setAccessToken(tokenUtils.generateAccessToken(userByEmail));
    loginResponse.setRefreshToken(tokenUtils.generateRefreshToken(userByEmail));
    return loginResponse;
  }

  public RegisterResponse registerUser(RegisterRequest request) {
    Optional<User> userOptional = userManagementService.getUserOptionalByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      throw EmailTakenException.of(String.format("Email %s already taken.", request.getEmail()));
    }

    User mappedUser = userMapper.getModel(request);
    mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
    mappedUser.setEmailConfirmed(false);

    User savedUser = userManagementService.createUser(mappedUser);

    emailService.sendRegisterConfirmation(savedUser.getEmail(),
        confirmationCodeService.generateRegistrationCode(savedUser));

    RegisterResponse registerResponse = userMapper.getRegisterResponse(savedUser);
    registerResponse.setAccessToken(tokenUtils.generateAccessToken(savedUser));
    registerResponse.setRefreshToken(tokenUtils.generateRefreshToken(savedUser));
    return registerResponse;
  }

  public ConfirmRegistrationResponse confirmRegistration(ConfirmRegistrationRequest request) {
    boolean isConfirmationCodeValid = confirmationCodeService.checkConfirmationCode(request);
    if(!isConfirmationCodeValid) {
      throw BadConfirmationCodeException.of("Confirmation code is not valid!");
    }
    User userByEmail = userManagementService.getUserByEmail(request.getEmail());
    userByEmail.setEmailConfirmed(true);
    userManagementService.updateUser(userByEmail);

    return ConfirmRegistrationResponse.builder()
        .accessToken(tokenUtils.generateAccessToken(userByEmail))
        .refreshToken(tokenUtils.generateRefreshToken(userByEmail))
        .message("Email confirmed!")
        .user(userMapper.getAuthUser(userByEmail))
        .build();
  }

  private void checkPassword(String rawPassword, String encodedPassword) {
    boolean passwordMatches = passwordEncoder.matches(rawPassword, encodedPassword);

    if (!passwordMatches) {
      throw BadPasswordException.of("Passwords not match!");
    }
  }

  private void checkEmailConfirmed(User user) {
    if (!user.isEmailConfirmed()) {
      throw EmailNotConfirmedException.of("Account's email: %s is not confirmed!");
    }
  }

  private void preventBruteForceAttack() {
    try {
      Thread.sleep(BRUTE_FORCE_PREVENTION_SLEEP_MILLIS);
    } catch (InterruptedException e) {
      throw new RuntimeException("An error occurred while preventing brute force attack.", e);
    }
  }
}
