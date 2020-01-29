package com.geovra.red.item;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ItemApi {
  @GET("items/")
  Observable<ItemIndexResponse> getItems(@Query("status") String status);
}
