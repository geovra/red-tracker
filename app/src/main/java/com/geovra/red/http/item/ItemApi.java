package com.geovra.red.http.item;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemApi {

  @GET("tracker/api/v1/items?api_token=3KAFStY16uBsbls1M")
  // @GET("users")
  // Observable<ItemResponse> getItems(@Query("status") String status);
  Observable<ItemResponse.ItemIndex> getItems(@Query("status") String status, @Header("Cookie") String cookie, @Header("User-Agent") String agent, @Header("Host") String host);

  @POST("tracker/api/v1/items?api_token=3KAFStY16uBsbls1M")
  @FormUrlEncoded
  Observable<ItemResponse.ItemStore> storeItem(
      @Header("Cookie") String cookie,
      @Field("title") String title,
      @Field("description") String description,
      @Field("status") String status );

}
