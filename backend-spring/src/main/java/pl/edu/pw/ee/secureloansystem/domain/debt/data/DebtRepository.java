package pl.edu.pw.ee.secureloansystem.domain.debt.data;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.secureloansystem.domain.debt.entity.Debt;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Repository
interface DebtRepository extends JpaRepository<Debt, Long> {
  Optional<Debt> findDebtByUser(User borrower);
}
