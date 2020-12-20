package com.geovra.red.app.persistence;

import android.content.Context;

import com.geovra.red.filter.persistence.FilterOutput;
import com.geovra.red.item.http.StatusResponse;
import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Headers;

public class RedCache {
  private RedPrefs prefs;

  /**
   * Wrap the cache contents into a retrofit2.Response object to match with the API return type
   *
   * @param ctx Context
   * @param key For example: TABLE_STATUS, TABLE_ITEMS, etc
   * @param cls The class to return: ItemResponse.ItemIndex
   * @return
   */
  public <T> Observable<Response<T>> get(Context ctx, String key, Class<T> cls) {
    RedPrefs prefs = new RedPrefs();
    String content = prefs.getString(ctx, key);

    T index = new Gson().fromJson(content, cls);

    return Observable
      .just(Response.success(index))
      .observeOn(Schedulers.io());
  }


  /**
   * Wrap the cache contents into a retrofit2.Response object to match with the API return type
   *
   * @param ctx Context
   * @param key For example: TABLE_STATUS, TABLE_ITEMS, etc
   * @param data The data to store: ItemResponse.ItemIndex
   * @param cls The class to store: ItemResponse.ItemIndex.class
   * @return
   */
  public <T> void set(Context ctx, String key, T data, Class<T> cls) {
    RedPrefs prefs = new RedPrefs();
    prefs.putString(ctx, key, new Gson().toJson(data));
  }
}
