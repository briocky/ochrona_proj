package pl.edu.pw.ee.secureloansystem.domain.loan.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanDto;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanLenderDto;
import pl.edu.pw.ee.secureloansystem.domain.loan.dto.LoanRequest;
import pl.edu.pw.ee.secureloansystem.domain.loan.entity.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {
  @Mapping(target = "lenderEmail", source = "lender.email")
  LoanDto getLoanDto(Loan source);

  @Mapping(target = "repaidAt", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "lender", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "borrower", ignore = true)
  Loan getModel(LoanRequest source);

  @Mapping(target = "borrowerEmail", source = "borrower.email")
  LoanLenderDto getLoanLenderDto(Loan source);
}
