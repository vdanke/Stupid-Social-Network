package com.step.stupid.social.network.service.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailConstraintValidator implements ConstraintValidator<EmailConstraint, String> {

   private final static String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

   private int min;
   private int max;

   public void initialize(EmailConstraint constraint) {
      this.min = constraint.min();
      this.max = constraint.max();
   }

   public boolean isValid(String email, ConstraintValidatorContext context) {
      if (StringUtils.isEmpty(email) || (email.length() < min) || (email.length() > max)) {
         return false;
      }
      Pattern validEmail = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

      Matcher emailMatcher = validEmail.matcher(email);

      return emailMatcher.find();
   }
}
