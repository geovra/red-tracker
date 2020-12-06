package com.geovra.red.item.http;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemApi {
  static String AUTHORIZATION_HEADER = "Bearer 9CvU9jq23vvaDkYZa9Z3Pr7TN9x1CBNH00slMYcf";

  @GET("api/v1/status?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemStatus>> getHeartbeat(@Header("Authorization") String bearer, @Query("_cookie") String _cookie);


  @GET("api/v1/items?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemIndex>> getItems(
    @Query("status") String status,
    @Header("Authorization") String bearer,
    @Header("User-Agent") String agent,
    @Header("Host") String host );

  @GET("api/v1/items/interval/{name}?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemIndex>> getItemsByInterval(
    @Header("Authorization") String bearer,
    @Path("name") String name,
    @Query("per_page") int perPage );

  @FormUrlEncoded
  @POST("api/v1/items?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemStore>> storeItem(
    @Header("Authorization") String bearer,
    @Field("title") String title,
    @Field("description") String description,
    @Field("status") int status,
    @Field("is_continuous") String is_continous,
    @Field("complexity") int complexity,
    @Field("date") String date );


  @FormUrlEncoded
  @POST("api/v1/items/{id}?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemUpdate>> updateItem(
    @Header("Authorization") String bearer,
    @Path("id") int id,
    @Field("title") String title,
    @Field("description") String description,
    @Field("status") int status,
    @Field("is_continuous") String is_continuous,
    @Field("complexity") int complexity,
    @Field("date") String date,
    @Field("_method") String method  );


  @FormUrlEncoded
  @POST("api/v1/items/{id}?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemRemove>> removeItem(
    @Header("Authorization") String bearer,
    @Path("id") int id,
    @Field("_method") String method );


  @GET("api/v1/status?bearer=" + AUTHORIZATION_HEADER)
  Observable<Response<ItemResponse.ItemIndex>> heartbeat(@Query("status") int status, @Header("Authorization") String bearer, @Header("User-Agent") String agent, @Header("Host") String host);

}
