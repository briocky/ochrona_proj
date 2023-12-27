package pl.edu.pw.ee.secureloansystem.domain.loan.data;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanStatus;
import pl.edu.pw.ee.secureloansystem.domain.loan.entity.Loan;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Repository
interface LoanRepository extends JpaRepository<Loan, Long> {
  Page<Loan> findAllByBorrower(User borrower, Pageable pageable);
  Optional<Loan> findLoanByIdAndLenderAndStatus(
    Long loanId,
    User lender,
    LoanStatus status
  );
  Page<Loan> findAllByLender(User lender, Pageable pageable);
}
