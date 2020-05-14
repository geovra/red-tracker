package com.geovra.red.app.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface HttpMock {
  static String API_TOKEN = "3KAFStY16uBsbls1M";

  @GET("api/v1/status?api_token=3KAFStY16uBsbls1M")
  Call<String> getHeartbeat(@Header("Cookie") String cookie);

}
