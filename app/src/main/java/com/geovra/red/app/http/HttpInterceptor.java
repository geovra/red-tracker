package com.geovra.red.app.http;

import android.util.Log;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class HttpInterceptor implements Interceptor {
  @Override
  public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
    Request original = chain.request();
    Request request = original.newBuilder()
      // .header("Cookie", dCookie.getValue() + "xxx")
      .method(original.method(), original.body())
      .build();

    okhttp3.Response response = chain.proceed(request);
    String body = response.body().string();
    Log.w("HttpInterceptor", String.format("[response] method:%s %s %s", request.method(), URLDecoder.decode(request.url().toString(), StandardCharsets.UTF_8.name()), body) );

    ResponseBody rb = ResponseBody.create(response.body().contentType(), body);
    return response.newBuilder().body(rb).build();
  }
}
