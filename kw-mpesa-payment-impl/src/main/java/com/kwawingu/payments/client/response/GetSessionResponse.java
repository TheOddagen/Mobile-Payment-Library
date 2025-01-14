/*
 * Copyright 2021-2024 KwaWingu.
 */
package com.kwawingu.payments.client.response;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.kwawingu.payments.session.keys.MpesaSessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetSessionResponse extends MpesaHttpResponse {
  @SuppressWarnings("UnusedVariable")
  private static final Logger LOG = LoggerFactory.getLogger(GetSessionResponse.class);

  @SerializedName("output_SessionID")
  private final MpesaSessionKey output_SessionID;

  private final int httpStatusCode;

  public GetSessionResponse(int statusCode, JsonObject jsonObject) {
    super(
        jsonObject.get("output_ResponseCode").getAsString(),
        jsonObject.get("output_ResponseDesc").getAsString());
    output_SessionID = new MpesaSessionKey(jsonObject.get("output_SessionID").getAsString());
    httpStatusCode = statusCode;
  }

  public MpesaSessionKey getOutput_SessionID() {
    return output_SessionID;
  }

  public int getHttpStatusCode() {
    return httpStatusCode;
  }
}
