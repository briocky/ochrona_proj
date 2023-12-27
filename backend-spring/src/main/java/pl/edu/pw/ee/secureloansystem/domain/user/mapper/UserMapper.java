package pl.edu.pw.ee.secureloansystem.domain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.AuthUser;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.LoginResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterRequest;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterResponse;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "loans", ignore = true)
  User getModel(RegisterRequest source);

  RegisterResponse getRegisterResponse(User user);

  LoginResponse getLoginResponse(User user);

  AuthUser getAuthUser(User user);
}
