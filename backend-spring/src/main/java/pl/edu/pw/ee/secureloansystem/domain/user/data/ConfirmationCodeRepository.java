package pl.edu.pw.ee.secureloansystem.domain.user.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.ConfirmationCode;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
  Optional<ConfirmationCode> findByUserEmail(String email);
}
