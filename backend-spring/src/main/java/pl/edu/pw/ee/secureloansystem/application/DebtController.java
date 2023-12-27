package pl.edu.pw.ee.secureloansystem.application;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.ee.secureloansystem.domain.debt.data.DebtService;
import pl.edu.pw.ee.secureloansystem.domain.debt.dto.DebtDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/debt")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
class DebtController {

  final DebtService debtService;

  @GetMapping("/all")
  public ResponseEntity<Page<DebtDto>> getAllDebts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return ResponseEntity.ok(debtService.getAllDebts(pageRequest));
  }
}
