package com.geovra.red.item.http;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CategoryApi {

  @GET("api/v1/categories")
  Observable<Response<CategoryResponse.CategoryIndex>> getItemsByInterval(
    @Header("Authorization") String bearer,
    @Query("per_page") int perPage
  );
}
