package com.geovra.red.category.persistence;

import android.content.Context;

import com.geovra.red.app.http.RetrofitApi;
import com.geovra.red.app.persistence.RedCache;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.app.service.RedService;
import com.geovra.red.category.http.CategoryApi;
import com.geovra.red.category.http.CategoryResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

public class CategoryRepo extends RedService {
  private static final String TAG = "CategoryRepo";
  public CategoryApi api;
  @Getter @Setter private RedPrefs prefs;
  @Getter @Setter private RedCache cache;

  public CategoryRepo(Context ctx) {
    this.prefs = new RedPrefs();
    this.cache = new RedCache();
    this.api = RetrofitApi.create(ctx, CategoryApi.class, prefs);
  }

  /**
   * Keep data from cache if present or make http request
   *
   * @param ctx Context
   * @return
   */
  public Observable<Response<CategoryResponse.CategoryIndex>> findAll(Context ctx)
  {
    Observable<Response<CategoryResponse.CategoryIndex>> cacheResponse = cache.get(ctx, "TABLE_CATEGORIES", CategoryResponse.CategoryIndex.class);

    return cacheResponse
      .flatMap(response -> {
        return (null != response.body()) && response.body().getData().size() > 0
          ? Observable.just(response)
          : api.index(prefs.getString(ctx, "BEARER_TOKEN"));
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }
}
