/*
 * Copyright 2021-2023 KwaWingu.
 */
package com.kwawingu.payments;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import com.kwawingu.payments.session.Config;
import com.kwawingu.payments.session.MpesaKeyProvider;
import com.kwawingu.payments.session.MpesaKeyProviderFromEnvironment;
import com.kwawingu.payments.session.keys.MpesaEncryptedApiKey;
import com.kwawingu.payments.session.keys.MpesaPublicKey;
import com.kwawingu.payments.session.keys.MpesaSessionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionKeyGeneratorTest {
  private static final Logger LOG = LoggerFactory.getLogger(SessionKeyGeneratorTest.class);

  private SessionKeyGenerator mpesaSessionKeyGenerator;
  private ApiEndpoint apiEndpoint;
  private MpesaKeyProvider keyProvider;

  @BeforeEach
  public void setUp() {
    Config config = new Config.Builder()
            .setMpesaApiKey("MPESA_API_KEY")
            .setMpesaPublicKey("MPESA_PUBLIC_KEY")
            .build();
    mpesaSessionKeyGenerator = new SessionKeyGenerator();
    apiEndpoint = new ApiEndpoint(Environment.SANDBOX, Market.VODACOM_TANZANIA);
    keyProvider = new MpesaKeyProviderFromEnvironment(config);
  }

  @Test
  public void testClientGetSessionKey() {

    String context = apiEndpoint.getUrl(Service.GET_SESSION);
    LOG.info(context);

    MpesaEncryptedApiKey encryptedApiKey = null;

    try {
      encryptedApiKey = keyProvider.getApiKey().encrypt(keyProvider.getPublicKey());
    } catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException | NoSuchPaddingException |
             InvalidKeyException | IllegalBlockSizeException e) {
      throw new RuntimeException(e);
    }

    assertNotNull(encryptedApiKey);

    MpesaSessionKey sessionKey = mpesaSessionKeyGenerator.getSessionKeyOrThrowUnchecked(encryptedApiKey, context);

    assertNotNull(sessionKey);
  }
}
