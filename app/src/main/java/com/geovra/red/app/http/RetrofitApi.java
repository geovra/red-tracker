package com.geovra.red.app.http;

import android.content.Context;

import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.item.http.ItemApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
  public static <T> T create(Context ctx, Class<T> c, RedPrefs prefs) {
    Gson gson = new GsonBuilder()
      .setLenient()
      .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
      .connectTimeout(1, TimeUnit.MINUTES)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS)
      // Log the response string
      .addInterceptor(new HttpInterceptor())
      .build();

    return new Retrofit.Builder()
      .baseUrl(prefs.getString(ctx, "API_BASE_URL"))
      .client(okHttpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
      .create(c);
  }
}
