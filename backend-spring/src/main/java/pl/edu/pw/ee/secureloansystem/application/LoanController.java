package pl.edu.pw.ee.secureloansystem.application;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ee.secureloansystem.domain.loan.data.LoanService;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanDto;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanLenderDto;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanRequest;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanRequestAction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
class LoanController {

  final LoanService loanService;

  @GetMapping("/my/all")
  public ResponseEntity<Page<LoanDto>> getAllUserLoans(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return ResponseEntity.ok(loanService.getAllUserLoans(pageRequest));
  }

  @PostMapping("/new")
  public ResponseEntity<LoanDto> makeNewLoan(@RequestBody @Valid LoanRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(loanService.makeNewLoan(request));
  }

  @PutMapping("/{id}/respond")
  public ResponseEntity<LoanDto> respondToLoanRequest(
      @PathVariable("id") Long loanId,
      @RequestParam("action") LoanRequestAction action) {
    return ResponseEntity.ok(loanService.respondToLoanRequest(loanId, action));
  }

  @GetMapping("/requests/all")
  public ResponseEntity<Page<LoanLenderDto>> getAllLoanRequest(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by("status"));
    return ResponseEntity.ok(loanService.getAllLoanRequests(pageRequest));
  }

  @GetMapping("/{id}/payback")
  public ResponseEntity<LoanDto> paybackTheLoan(@PathVariable("id") Long id) {
    return ResponseEntity.ok(loanService.paybackTheLoan(id));
  }
}
