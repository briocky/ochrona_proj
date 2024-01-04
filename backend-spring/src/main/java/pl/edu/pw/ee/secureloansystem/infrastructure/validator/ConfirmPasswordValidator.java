package pl.edu.pw.ee.secureloansystem.infrastructure.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.pw.ee.secureloansystem.domain.user.dto.RegisterRequest;

class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, RegisterRequest> {

  private String message;

  @Override
  public void initialize(ConfirmPassword constraintAnnotation) {
    this.message = constraintAnnotation.message();
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
    final String password = request.getPassword();
    final String confirmPassword = request.getConfirmPassword();
    final boolean passwordsMatch = password.equals(confirmPassword);

    if (!passwordsMatch) {
      context.buildConstraintViolationWithTemplate(message).addPropertyNode("confirmPassword")
          .addConstraintViolation();
    }

    return passwordsMatch;
  }
}
