package pl.edu.pw.ee.secureloansystem.infrastructure.security.encoding;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Slf4j
public class MyPasswordEncoder implements PasswordEncoder {

  private static final int SALT_LENGTH = 8;
  private static final int ITERATIONS = 10000;
  private static final String ALGORITHM_NAME = "SHA-256";

  @Value("${security.pepper}")
  private String pepper;

  @Override
  public String encode(CharSequence rawPassword) {
    ByteBuffer salt = generateSalt();
    ByteBuffer pepperBytes = ByteBuffer.wrap(pepper.getBytes(StandardCharsets.UTF_8));
    ByteBuffer rawPasswordBytes = ByteBuffer
        .wrap(rawPassword.toString().getBytes(StandardCharsets.UTF_8));

    ByteBuffer saltedPassword = addBytesToStart(salt, rawPasswordBytes);
    ByteBuffer pepperedPassword = addBytesToEnd(pepperBytes, saltedPassword);
    ByteBuffer hashedPassword = hashPassword(pepperedPassword);

    ByteBuffer finalPassword = addBytesToStart(salt, hashedPassword);

    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(finalPassword.array());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    ByteBuffer rawPasswordBytes = ByteBuffer
        .wrap(rawPassword.toString().getBytes(StandardCharsets.UTF_8));
    ByteBuffer decodedPassword = ByteBuffer.wrap(Base64.getDecoder().decode(encodedPassword));

    byte[] hashedPasswordArray = new byte[decodedPassword.capacity() - SALT_LENGTH];
    byte[] saltArray = new byte[SALT_LENGTH];
    decodedPassword.get(saltArray);
    ByteBuffer salt = ByteBuffer.wrap(saltArray);
    ByteBuffer pepperBytes = ByteBuffer.wrap(pepper.getBytes(StandardCharsets.UTF_8));

    ByteBuffer saltedPassword = addBytesToStart(salt, rawPasswordBytes);
    ByteBuffer pepperedPassword = addBytesToEnd(pepperBytes, saltedPassword);
    ByteBuffer hashedPassword = hashPassword(pepperedPassword);

    decodedPassword.get(SALT_LENGTH, hashedPasswordArray);

    return hashedPassword.equals(ByteBuffer.wrap(hashedPasswordArray));
  }

  private static ByteBuffer addBytesToStart(ByteBuffer content, ByteBuffer destination) {
    int bufferSize = content.capacity() + destination.capacity();
    ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

    byteBuffer.put(content.array());
    byteBuffer.put(destination.array());

    return byteBuffer;
  }

  private static ByteBuffer addBytesToEnd(ByteBuffer content, ByteBuffer destination) {
    int bufferSize = content.capacity() + destination.capacity();
    ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

    byteBuffer.put(destination.array());
    byteBuffer.put(content.array());

    return byteBuffer;
  }

  private static ByteBuffer generateSalt() {
    byte[] salt = new byte[SALT_LENGTH];
    new SecureRandom().nextBytes(salt);
    log.debug("Generated salt: {}", salt);
    return ByteBuffer.wrap(salt);
  }

  private static ByteBuffer hashPassword(ByteBuffer password) {
    try {
      MessageDigest digest = MessageDigest.getInstance(ALGORITHM_NAME);
      digest.reset();

      byte[] hashedBytes = digest.digest(password.array());
      for (int i = 0; i < ITERATIONS - 1; i++) {
        digest.reset();
        hashedBytes = digest.digest(hashedBytes);
      }

      return ByteBuffer.wrap(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Hashing algorithm not found", e);
    }
  }
}
