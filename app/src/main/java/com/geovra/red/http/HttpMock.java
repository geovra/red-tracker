package com.geovra.red.http;

import com.geovra.red.http.item.ItemResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpMock {
  static String API_TOKEN = "3KAFStY16uBsbls1M";

  @GET("api/v1/status?api_token=3KAFStY16uBsbls1M")
  Call<String> getHeartbeat(@Header("Cookie") String cookie);

}
