package pl.edu.pw.ee.secureloansystem.domain.user.data;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.ConfirmRegistrationRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.ConfirmationCode;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.NoConfirmationCodeFoundException;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class ConfirmationCodeService {

  final ConfirmationCodeRepository confirmationCodeRepository;

  public boolean checkConfirmationCode(ConfirmRegistrationRequest request) {
    ConfirmationCode userCode = confirmationCodeRepository.findByUserEmail(request.getEmail())
        .orElseThrow(() ->  NoConfirmationCodeFoundException.of(
            String.format("%s does not have a confirmation code!", request.getEmail())));
    return userCode.getConfirmationCode().equals(request.getCode());
  }

  public String generateRegistrationCode(User user) {
    final Random random = new Random(System.currentTimeMillis());

    final int minRange = 100000;
    final int maxRange = 999999;

    int confirmationCodeValue = random.nextInt((maxRange - minRange) + 1) + minRange;
    String codeString = String.valueOf(confirmationCodeValue);
    saveConfirmationCode(codeString, user);

    return String.valueOf(confirmationCodeValue);
  }

  private void saveConfirmationCode(String confirmationCode, User user) {
    ConfirmationCode code = ConfirmationCode.builder()
        .confirmationCode(confirmationCode)
        .user(user)
        .build();
    confirmationCodeRepository.save(code);
  }
}
