package pl.edu.pw.ee.secureloansystem.domain.user.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
