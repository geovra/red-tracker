package com.geovra.red.status.http;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface StatusApi {

  @GET("api/v1/status")
  Observable<Response<StatusResponse.StatusIndex>> index(
    @Header("Authorization") String bearer
  );
}
