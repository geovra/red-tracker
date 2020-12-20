package com.geovra.red.item.service;

import android.content.Context;
import android.util.Log;

import com.geovra.red.app.http.HttpInterceptor;
import com.geovra.red.app.http.RetrofitApi;
import com.geovra.red.app.persistence.RedCache;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.item.http.ItemApi;
import com.geovra.red.item.http.StatusApi;
import com.geovra.red.item.http.StatusResponse;
import com.geovra.red.item.persistence.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatusService {
  private static final String TAG = "StatusService";
  public StatusApi api;
  @Getter @Setter private RedPrefs prefs;
  @Getter @Setter private RedCache cache;

  public StatusService() {
    this.prefs = new RedPrefs();
    this.cache = new RedCache();
    this.api = RetrofitApi.create(StatusApi.class);
  }

  /**
   * Keep data from cache if present or make http request
   *
   * @param ctx Context
   * @return
   */
  public Observable<Response<StatusResponse.StatusIndex>> findAll(Context ctx)
  {
    Observable<Response<StatusResponse.StatusIndex>> cacheResponse = cache.get(ctx, "TABLE_STATUS", StatusResponse.StatusIndex.class);

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
