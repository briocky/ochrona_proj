package pl.edu.pw.ee.secureloansystem.domain.debt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.pw.ee.secureloansystem.domain.debt.dto.DebtDto;
import pl.edu.pw.ee.secureloansystem.domain.debt.entity.Debt;

@Mapper(componentModel = "spring")
public interface DebtMapper {
  DebtDto getDebtDto(Debt source);
}
