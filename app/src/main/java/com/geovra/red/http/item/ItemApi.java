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

// api/v1/items/interval/w?type=d&page_size=10 ................ MAGIC from current week: 10 rows for every day - type=d
// api/v1/items/interval/m?type=w&page_size=10 ................ MAGIC from current month: 10 rows for every week - type=w
// api/v1/items/d/2020-03-27?&page=3&page_size=10 ............. 10 rows on 3rd page for day xxx
// api/v1/items/w/2020-03-27?&page=3&page_size=10 ............. 10 rows on 3rd page for week xxx
// api/v1/items/y/2020?&page=3&page_size=10 ................... 10 rows on 3rd page for year xxx
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

  @GET("tracker/api/v1/items/interval/{name}?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemIndex>> getItemsByInterval(
    @Header("Cookie") String cookie,
    @Path("name") String name,
    @Query("per_page") int perPage );

  @FormUrlEncoded
  @POST("tracker/api/v1/items?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemStore>> storeItem(
    @Header("Cookie") String cookie,
    @Field("title") String title,
    @Field("description") String description,
    @Field("status") int status,
    @Field("is_continuous") String is_continous,
    @Field("complexity") int complexity,
    @Field("date") String date );


  @FormUrlEncoded
  @POST("tracker/api/v1/items/{id}?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemUpdate>> updateItem(
    @Header("Cookie") String cookie,
    @Path("id") int id,
    @Field("title") String title,
    @Field("description") String description,
    @Field("status") int status,
    @Field("is_continuous") String is_continuous,
    @Field("complexity") int complexity,
    @Field("date") String date,
    @Field("_method") String method  );


  @FormUrlEncoded
  @POST("tracker/api/v1/items/{id}?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemRemove>> removeItem(
    @Header("Cookie") String cookie,
    @Path("id") int id,
    @Field("_method") String method );


  @GET("tracker/api/v1/status?api_token=" + API_TOKEN)
  Observable<Response<ItemResponse.ItemIndex>> heartbeat(@Query("status") int status, @Header("Cookie") String cookie, @Header("User-Agent") String agent, @Header("Host") String host);

}
