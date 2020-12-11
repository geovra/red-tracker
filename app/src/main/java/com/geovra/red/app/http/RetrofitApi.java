package com.geovra.red.app.http;

import com.geovra.red.item.http.ItemApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
  public static <T> T create(Class<T> c) {
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
      .baseUrl("http://geovra-tracker.herokuapp.com/") // .baseUrl("https://jsonplaceholder.typicode.com/")
      .client(okHttpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
      .create(c);
  }
}
