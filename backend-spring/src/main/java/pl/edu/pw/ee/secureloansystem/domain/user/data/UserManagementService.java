package pl.edu.pw.ee.secureloansystem.domain.user.data;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserManagementService {

  final UserRepository userRepository;

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> UserNotFoundException
        .of(String.format("User with email=%s could not be found", email)));
  }

  public Optional<User> getUserOptionalByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(User updatedUser) {
    return userRepository.save(updatedUser);
  }
}
