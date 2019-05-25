package utils;

import models.Group;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class Generators {
  // 128 bits key => 16 chars string
  private final int KEY_SIZE = 128;
  private static Generators instance;

  private KeyGenerator keyGenerator;
  private SecureRandom secureRandom = new SecureRandom();

  private Generators() {
    try {
      keyGenerator = KeyGenerator.getInstance("AES");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    keyGenerator.init(KEY_SIZE, secureRandom);
  }

  public static Generators getInstance() {
    if (instance == null) {
      instance = new Generators();
    }

    return instance;
  }

  public String generateSecret() {
    SecretKey key = keyGenerator.generateKey();
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

}
