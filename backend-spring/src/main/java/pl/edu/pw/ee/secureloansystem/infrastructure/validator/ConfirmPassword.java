package pl.edu.pw.ee.secureloansystem.infrastructure.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
public @interface ConfirmPassword {
  String message() default "Passwords are different";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
