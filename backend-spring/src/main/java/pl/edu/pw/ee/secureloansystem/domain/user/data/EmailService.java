package pl.edu.pw.ee.secureloansystem.domain.user.data;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("classpath:registration_template.html")
  private Resource templateResource;

  public void sendRegisterConfirmation(String to, String code) {
    final String template = loadRegistrationTemplate();
    String email = String.format(template, to, to, code);

    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper;
    try {
      helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      helper.setTo(to);
      helper.setSubject("Registration confirmation");
      helper.setText(email, true);
    } catch(MessagingException ex) {
      throw new RuntimeException("An error occurred while creating a email message", ex);
    }
    javaMailSender.send(message);
    log.debug("Email to {} has been sent", to);
  }

  private String loadRegistrationTemplate() {
    try (InputStreamReader reader = new InputStreamReader(templateResource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException ex) {
      throw new RuntimeException("Error while reading the email template.", ex);
    }
  }
}
