package pl.edu.pw.ee.secureloansystem.domain.debt.data;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.debt.dto.DebtDto;
import pl.edu.pw.ee.secureloansystem.domain.debt.entity.Debt;
import pl.edu.pw.ee.secureloansystem.domain.debt.mapper.DebtMapper;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanStatus;
import pl.edu.pw.ee.secureloansystem.domain.loan.entity.Loan;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DebtService {

  final DebtRepository debtRepository;
  final DebtMapper debtMapper;

  public void updateUserDebtInfo(Loan loan) {
    User borrower = loan.getBorrower();
    Optional<Debt> debtByUser = debtRepository.findDebtByUser(borrower);
    Debt debt;

    if (debtByUser.isPresent()) {
      debt = debtByUser.get();
      updateDebtAmount(debt, loan);
    } else {
      debt =
      Debt.builder().user(borrower).totalAmount(loan.getAmount()).build();
    }
    debtRepository.save(debt);
  }

  public Page<DebtDto> getAllDebts(PageRequest pageRequest) {
    Page<Debt> debts = debtRepository.findAll(pageRequest);
    return debts.map(debtMapper::getDebtDto);
  }

  private void updateDebtAmount(Debt debt, Loan loan) {
    double newTotalAmount = debt.getTotalAmount();

    if (loan.getStatus() == LoanStatus.REPAID) {
      newTotalAmount -= loan.getAmount();
    } else if (loan.getStatus() == LoanStatus.ACCEPTED) {
      newTotalAmount += loan.getAmount();
    }

    debt.setTotalAmount(newTotalAmount);
  }
}
