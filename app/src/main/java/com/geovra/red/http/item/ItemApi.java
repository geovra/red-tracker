package com.geovra.red.http.item;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ItemApi {
  static String API_TOKEN = "3KAFStY16uBsbls1M";

  @GET("tracker/api/v1/status?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemStatus>> getHeartbeat(@Header("Cookie") String cookie, @Query("_cookie") String _cookie);

  @GET("tracker/api/v1/items?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemIndex>> getItems(
    @Query("status") String status,
    @Header("Cookie") String cookie,
    @Header("User-Agent") String agent,
    @Header("Host") String host );

  @FormUrlEncoded
  @POST("tracker/api/v1/items?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemStore>> storeItem(
    @Header("Cookie") String cookie,
    @Field("title") String title,
    @Field("description") String description,
    @Field("status") String status );

  @FormUrlEncoded
  @POST("tracker/api/v1/items/{id}?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemRemove>> removeItem(
    @Header("Cookie") String cookie,
    @Path("id") int id,
    @Field("_method") String method);

  @GET("tracker/api/v1/status?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemIndex>> heartbeat(@Query("status") String status, @Header("Cookie") String cookie, @Header("User-Agent") String agent, @Header("Host") String host);

}
