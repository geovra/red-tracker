package com.geovra.red.comment.http;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentApi
{
  @FormUrlEncoded
  @POST("api/v1/items/{idItem}/comments")
  Observable<Response<CommentResponse.CommentStore>> store(
    @Header("Authorization") String bearer,
    @Path("idItem") int idItem,
    @Field("body") String body
  );


  @POST("api/v1/items/{idItem}/comments/{idComment}")
  Observable<Response<CommentResponse.CommentUpdate>> update(
    @Header("Authorization") String bearer,
    @Path("idItem") int idItem,
    @Path("idComment") int idComment,
    @Field("body") String body,
    @Field("_method") String method
  );


  @FormUrlEncoded
  @POST("api/v1/items/")
  Observable<Response<CommentResponse.CommentRemove>> remove(
    @Header("Authorization") String bearer,
    @Path("id") int id,
    @Field("_method") String method
  );
}
