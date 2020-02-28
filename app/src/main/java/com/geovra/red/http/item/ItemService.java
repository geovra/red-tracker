package com.geovra.red.http.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.R;
import com.geovra.red.RedService;
import com.geovra.red.model.Item;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("CheckResult")
public class ItemService {
  public RedService sRed;
  public static final String TAG = "ItemService";
  public ItemApi api;
  protected static String API_COOKIE = "__test=38dd9cea823677c94202240bd7b02ed2; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/";

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
        .build();

    api = new Retrofit.Builder()
        .baseUrl("http://geovra-php.rf.gd/") // .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(ItemApi.class);
  }


  public Observable<ItemResponse.ItemIndex> findAll()
  {
    // __test="+toHex(slowAES.decrypt(c,2,a,b))+
    return api.getItems(
        null,
        API_COOKIE,
        "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:73.0) Gecko/20100101 Firefox/73.0",
        "geovra-php.rf.gd"
        )
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


  public Observable<ItemResponse.ItemStore> store(Item item)
  {
    Log.d(TAG, "store");

    return api.storeItem(
        API_COOKIE,
        item.getTitle(),
        item.getDescription(),
        item.getStatus() )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }
}
