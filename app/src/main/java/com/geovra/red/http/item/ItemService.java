package com.geovra.red.http.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.R;
import com.geovra.red.RedService;
import com.geovra.red.http.HttpMock;
import com.geovra.red.http.RequestBag;
import com.geovra.red.model.Item;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("CheckResult")
public class ItemService {
  public RedService sRed;
  private AppCompatActivity ctx;
  public static final String TAG = "ItemService";
  public ItemApi api;
  private String heartbeatCookie;
  public static String API_COOKIE_HOME = "__test=3b64e99abae722cd892566de727a09e0; XSRF-TOKEN=eyJpdiI6Imgzd2lKeHFJU05GUEpXXC8zbGRsQThRPT0iLCJ2YWx1ZSI6ImpWN3J1RXk1MlB2dGxUVVU1R2dNbkZqNXcybmx2NWR0bXBZQ0duMHU3RWVLdGtBeFVkckJCMmcyTlc0Z0ZwdnoiLCJtYWMiOiI0YzRhODEwZTRiZDgwOGNlMGMyMmZhMGU0MDFhOTgwOTU0YWExYTg2ZGI0YmM1YzFlZWM2Mzg3YjZhNDkxMzRlIn0%3D; laravel_session=eyJpdiI6IlNtQkxUSElRRjdKbTdlQWliZ3VyR0E9PSIsInZhbHVlIjoiVmxiQXhaWE9OWlR2Z1wvSWl5Q1M4MVhmRGU4MkY4M1JaZWNwWGk0QitUcWNRVVBZZlwvdXhQWTRjY01ESmtUUEVvIiwibWFjIjoiZmY2NjllYjFmMWYzYTFiOTYxNjkyZmE1YTNlNjA2NGU1ZTFhNTcwYjY2YTA4MzI2ZGI3MjJiZDU2ZmZkODA0OSJ9";
  public static String API_COOKIE_WORK = "__test=38dd9cea823677c94202240bd7b02ed2;";
  public static String API_COOKIE_SIM = "__test=38dd9cea823677c94202240bd7b02ed2; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/";
  private MutableLiveData<String> dCookie = new MutableLiveData<>();

  public ItemService()
  {
    sRed = new RedService();

    Gson gson = new GsonBuilder()
        .setLenient()
        .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        // 500 Useful for static headers
        // .addInterceptor(new Interceptor() {
        //   @Override
        //   public okhttp3.Response intercept(Chain chain) throws IOException {
        //     Request original = chain.request();
        //     Request request = original.newBuilder()
        //       .header("Cookie", dCookie.getValue())
        //       .method(original.method(), original.body())
        //       .build();
        //
        //     return chain.proceed(request);
        //   }
        // })
        .build();

    api = new Retrofit.Builder()
        .baseUrl("http://geovra-php.rf.gd/") // .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ItemApi.class);
  }


  public <T extends AppCompatActivity> ItemService(T ctx) {
    this();
    this.ctx = ctx;
  }


  public Observable<Response<ItemResponse.ItemIndex>> findAll(RequestBag bag)
  {
    // __test="+toHex(slowAES.decrypt(c,2,a,b))+
    return api.getItems(
        null,
        dCookie.getValue(),
        "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0",
        "geovra-php.rf.gd"
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemStore>> store(Item item)
  {
    Log.d(TAG, "store");

    return api.storeItem(
        dCookie.getValue(),
        item.getTitle(),
        item.getDescription(),
        item.getStatus() )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemStatus>> heartbeat()
  {
    // __test="+toHex(slowAES.decrypt(c,2,a,b))+
    Observable<Response<ItemResponse.ItemStatus>> cookieHome = api.getHeartbeat(API_COOKIE_HOME).onErrorResumeNext(Observable.empty());
    Observable<Response<ItemResponse.ItemStatus>> cookieWork = api.getHeartbeat(API_COOKIE_WORK).onErrorResumeNext(Observable.empty());
    Observable<Response<ItemResponse.ItemStatus>> cookieSim = api.getHeartbeat(API_COOKIE_SIM).onErrorResumeNext(Observable.empty());

    return cookieHome
      .mergeWith(cookieWork)
      .mergeWith(cookieSim)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }


  public int setTabs(LayoutInflater inflater, TabLayout tabLayout)
  {
    // mv.getInterval => [ "2019-08-11", "2019-08-12", "2019-08-13", ... ]
    // repo.getItems => [ {0}, {1}, {2}, {3}, {4}, {5}, ... ]

    int tabTodayIndex = -1;
    final String today = sRed.getToday();
    ArrayList<String> days = sRed.getIntervalDays();

    for (int i = 0; i < days.size(); i++) {
      System.out.println("zzz " + today + days.toString());
      TabLayout.Tab tab = tabLayout.newTab();

      boolean isToday = today.equals(days.get(i));
      int resId = isToday ? R.layout.tab_main_day : R.layout.tab_main_day_0;
      if (isToday) { tabTodayIndex = i; }

      View view = getTabCustomView( inflater, days.get(i), resId, null );
      tab.setCustomView(view);
      tab.setTag(today);

      tabLayout.addTab(tab);
    }

    return tabTodayIndex;
  }


  public View getTabCustomView(LayoutInflater inflater, String day /* dd-MM-YYYY */, int layoutId, TabLayout.Tab tab)
  {
    // final LayoutInflater inflater = LayoutInflater.from(ctx);
    View view = inflater.inflate(layoutId, null);
    Pair<String, String> info = getTabInformation(day);

    TextView txName = (TextView) view.findViewById(R.id.int_day_name);
    TextView txNum = (TextView) view.findViewById(R.id.int_day_num);
    txName.setText( info.first );
    txNum.setText( info.second );

    return view;
  }


  public Pair<String, String> getTabInformation(String date /* dd-MM-YYYY */)
  {
    String e = sRed.getDayOfWeek(date);
    String d = sRed.getDayOfMonth(date);
    return new Pair<>(e, d);
  }


  public <T extends AppCompatActivity> void toCreate(T ctx, Class<?> cls)
  {
    Intent intent = new Intent(ctx, cls);
    ctx.startActivity(intent);
  }


  public <T extends AppCompatActivity> Item getItemFake(T ctx)
  {
    Item model = new Item();
    try {
      Intent intent = ctx.getIntent();
      Gson gson = new Gson();
      model = gson.fromJson(
          intent.getStringExtra("item"),
          Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    return model;
  }


  public void setCookie(String cookie)
  {
    dCookie.setValue(cookie);
  }
}
