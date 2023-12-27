package pl.edu.pw.ee.secureloansystem.domain.loan.data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.pw.ee.secureloansystem.domain.debt.data.DebtService;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.*;
import pl.edu.pw.ee.secureloansystem.domain.loan.entity.Loan;
import pl.edu.pw.ee.secureloansystem.domain.loan.mapper.LoanMapper;
import pl.edu.pw.ee.secureloansystem.domain.user.data.CurrentUserService;
import pl.edu.pw.ee.secureloansystem.domain.user.data.UserManagementService;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.AuthUser;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.IncorrectLoanOwnerException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.LoanNotFoundException;
import pl.edu.pw.ee.secureloansystem.infrastructure.exception.UnknownLoanRequestAction;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoanService {

  static final String ZONE_ID = "Europe/Warsaw";

  final CurrentUserService currentUserService;
  final LoanRepository loanRepository;
  final UserManagementService userManagementService;
  final LoanMapper loanMapper;
  final DebtService debtService;

  public LoanDto makeNewLoan(LoanRequest request) {
    Loan loan = loanMapper.getModel(request);
    User borrower = userManagementService.getUserByEmail(
      currentUserService.getCurrentUser().getEmail()
    );
    User lender = userManagementService.getUserByEmail(
      request.getLenderEmail()
    );
    loan.setBorrower(borrower);
    loan.setLender(lender);
    loan.setStatus(LoanStatus.WAITING);
    loan.setCreatedAt(LocalDate.now(ZoneId.of(ZONE_ID)));

    Loan savedLoan = loanRepository.save(loan);
    return loanMapper.getLoanDto(savedLoan);
  }

  public Page<LoanDto> getAllUserLoans(PageRequest pageRequest) {
    AuthUser currentUser = currentUserService.getCurrentUser();
    Page<Loan> userLoans = loanRepository.findAllByBorrower(
      userManagementService.getUserByEmail(currentUser.getEmail()),
      pageRequest
    );
    return userLoans.map(loanMapper::getLoanDto);
  }

  public LoanDto respondToLoanRequest(Long loanId, LoanRequestAction action) {
    Optional<Loan> loanToRespond =
      loanRepository.findLoanByIdAndLenderAndStatus(
        loanId,
        userManagementService.getUserByEmail(
          currentUserService.getCurrentUser().getEmail()
        ),
        LoanStatus.WAITING
      );

    if (loanToRespond.isPresent()) {
      return loanMapper.getLoanDto(respondToLoan(loanToRespond.get(), action));
    }

    throw LoanNotFoundException.of(
      String.format("Loan id=%d could not be found", loanId)
    );
  }

  public Page<LoanLenderDto> getAllLoanRequests(PageRequest pageRequest) {
    String currentUserEmail = currentUserService.getCurrentUser().getEmail();
    Page<Loan> loanRequests = loanRepository.findAllByLender(
      userManagementService.getUserByEmail(currentUserEmail),
      pageRequest
    );
    return loanRequests.map(loanMapper::getLoanLenderDto);
  }

  public LoanDto paybackTheLoan(Long id) {
    String currentUserEmail = currentUserService.getCurrentUser().getEmail();
    Loan loan = loanRepository
      .findById(id)
      .orElseThrow(() ->
        LoanNotFoundException.of(String.format("Loan id=%d not found", id))
      );

    if (loan.getStatus().equals(LoanStatus.ACCEPTED)) {
      return loanMapper.getLoanDto(repayLoan(loan, currentUserEmail));
    } else {
      throw LoanNotFoundException.of(
        String.format("Loan id=%d is not accepted!", id)
      );
    }
  }

  private Loan respondToLoan(Loan loan, LoanRequestAction action) {
    switch (action) {
      case ACCEPT -> {
        loan.setStatus(LoanStatus.ACCEPTED);
        debtService.updateUserDebtInfo(loan);
      }
      case REJECT -> loan.setStatus(LoanStatus.REJECTED);
      default -> throw UnknownLoanRequestAction.of(
        String.format("%s action is unknown", action)
      );
    }
    return loanRepository.save(loan);
  }

  private Loan repayLoan(Loan loan, String currentUserEmail) {
    if (loan.getBorrower().getEmail().equals(currentUserEmail)) {
      loan.setStatus(LoanStatus.REPAID);
      loan.setRepaidAt(LocalDate.now(ZoneId.of(ZONE_ID)));

      debtService.updateUserDebtInfo(loan);

      return loanRepository.save(loan);
    } else {
      throw IncorrectLoanOwnerException.of(
        String.format("%s is not the owner of this loan", currentUserEmail)
      );
    }
  }
}
