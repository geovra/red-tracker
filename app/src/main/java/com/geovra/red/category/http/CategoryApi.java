package com.geovra.red.category.http;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CategoryApi {

  @GET("api/v1/categories")
  Observable<Response<CategoryResponse.CategoryIndex>> index(
    @Header("Authorization") String bearer
  );
}
