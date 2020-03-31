package com.geovra.red.http.item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.R;
import com.geovra.red.RedService;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.Status;
import com.geovra.red.persistence.RedPrefs;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
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
  // public static String API_COOKIE_HOME = "__test=3b64e99abae722cd892566de727a09e0;";
  public static String API_COOKIE_HOME = "__test=71d481bc6d5c292f794b6a2c690f7bc4;";
  public static String API_COOKIE_WORK = "__test=38dd9cea823677c94202240bd7b02ed2;";
  public static String API_COOKIE_SIM = "__test=bf3b2611f756a1fbdd495a4e6711ee53;";
  private MutableLiveData<String> dCookie = new MutableLiveData<>();
  public enum ACTION_TYPE { CREATE, READ, UPDATE, DELETE };

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
        .addInterceptor(new Interceptor() {
          @Override
          public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
              // .header("Cookie", dCookie.getValue() + "xxx")
              .method(original.method(), original.body())
              .build();

            okhttp3.Response response = chain.proceed(request);
            String body = response.body().string();
            Log.w(TAG, String.format("[response] %s %s %s", request.method(), request.url().toString(), body) );

            ResponseBody rb = ResponseBody.create(response.body().contentType(), body);
            return response.newBuilder().body(rb).build();
          }
        })
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

    // dCookie.observe(ctx, value -> {
    //   Log.d(TAG, value);
    // });
  }


  public Observable<Response<ItemResponse.ItemIndex>> findAll(String interval)
  {
    // __test="+toHex(slowAES.decrypt(c,2,a,b))+
    return api.getItemsByInterval(
        dCookie.getValue(),
        interval,
        10 )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemStore>> store(Item item)
  {
    return api.storeItem(
      dCookie.getValue(),
      item.getTitle(),
      item.getDescription(),
      item.getStatus(),
      item.getIsContinuous(),
      item.getDate() )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemUpdate>> update(Item item)
  {
    return api.updateItem(
      dCookie.getValue(),
      item.getId(),
      item.getTitle(),
      item.getDescription(),
      item.getStatus(),
      item.getIsContinuous(),
      item.getDate(),
      "PUT" )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemRemove>> remove(Item item)
  {
    return api.removeItem(
      dCookie.getValue(),
      item.getId(),
      "DELETE")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
  }


  public void heartbeat(Function<Response<ItemResponse.ItemStatus>, Void> cb)
  {
    Consumer<Response<ItemResponse.ItemStatus>> doRes = res -> {
      cb.apply(res);
    };
    Consumer<Throwable> doErr = err -> {
      Log.e(TAG, err.toString());
    };

    api.getHeartbeat(API_COOKIE_HOME, API_COOKIE_HOME)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(doRes, doErr);

    api.getHeartbeat(API_COOKIE_WORK, API_COOKIE_WORK)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(doRes, doErr);

    api.getHeartbeat(API_COOKIE_SIM, API_COOKIE_SIM)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(doRes, doErr);

    // Observable.concat(cookieHome, cookieWork, cookieSim)
    //   .subscribeOn(Schedulers.io())
    //   .observeOn(AndroidSchedulers.mainThread())
    //   .doOnNext(res -> {
    //     Log.d(TAG, "...");
    //   })
    //   .subscribe();

    // final ArrayList<Integer> result;
    // cookieHome
    //   .subscribeOn(Schedulers.io())
    //   .flatMap(res -> {
    //     result.add(1);
    //     return cookieWork;
    //   })
    //   .flatMap(res -> {
    //     result.add(2);
    //     return cookieSim;
    //   })
    //   .map(res -> {
    //     result.add(3);
    //     return cookieHome;
    //   })
    //   .observeOn(AndroidSchedulers.mainThread())
    //   .subscribe(
    //     (res) -> {
    //       Log.d(TAG, "200");
    //     },
    //     (err) -> {
    //       Log.d(TAG, "200");
    //     },
    //     () -> {
    //       Log.d(TAG, "200");
    //     }
    //   );
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
    Item model = null;
    try {
      Intent intent = ctx.getIntent();
      Gson gson = new Gson();
      model = gson.fromJson(
          intent.getStringExtra("item"),
          Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    if (model == null) {
      model = new Item();
      model.setTitle("Choose firm for kitchen furniture");
      model.setDescription("a) Find firm \nb) Call them for an offer");
      model.setStatus(1);
    }

    return model;
  }


  public String getCookie()
  {
    return dCookie.getValue();
  }


  public void setCookie(Activity  activity, String cookie)
  {
    dCookie.setValue(cookie);
    RedPrefs.putString(activity, "COOKIE", cookie);
  }


  public Drawable setItemStatus(View img, Resources resources, Item item)
  {
    Drawable background = resources.getDrawable(R.drawable.shape_circle);

    if (item.getComplexity() == Item.COMPLEXITY_HARD)
      background = resources.getDrawable(R.drawable.shape_rect);

    if (item.getComplexity() == Item.COMPLEXITY_FUCK) {
      background = resources.getDrawable(R.drawable.shape_triangle);

      ViewGroup.LayoutParams params = img.getLayoutParams();
      float factor = resources.getDisplayMetrics().density;
      params.width = (int) (28 * factor);
      params.height = (int) (34 * factor);
      img.setTranslationX(4 * factor);
      img.setTranslationY(-12 * factor);
      if (img.getId() == R.id.status_img) { // Hack alert, fuuuck!
        img.setTranslationX(-2 * factor);
        img.setTranslationY(-13 * factor);
      }
      img.setLayoutParams(params);
    }

    // Colors
    background.setColorFilter(resources.getColor(R.color.yellowPrimary), PorterDuff.Mode.SRC_IN);

    if (item.getStatus() == Item.STATUS_URGENT)
      background.setColorFilter(resources.getColor(R.color.redPrimary), PorterDuff.Mode.SRC_IN);

    if (item.getStatus() == Item.STATUS_HUH)
      background.setColorFilter(resources.getColor(R.color.bluePrimary), PorterDuff.Mode.SRC_IN);

    if (item.getStatus() == Item.STATUS_POSTPONED)
      background.setColorFilter(resources.getColor(R.color.tonePrimary), PorterDuff.Mode.SRC_IN);

    if (item.getStatus() == Item.STATUS_ADDED)
      background.setColorFilter(resources.getColor(R.color.greyDimmer), PorterDuff.Mode.SRC_IN);

    if (item.getStatus() == Item.STATUS_COMPLETED)
      background.setColorFilter(resources.getColor(R.color.greenPrimary), PorterDuff.Mode.SRC_IN);

    // Icon
    img.setBackground(background);

    return background;
  }


  public String setItemStatus(TextView text, Resources resources, Item item)
  {
    String name = null;
    try {
      int id = resources.getIdentifier("status_" + String.valueOf(item.getStatus()), "string", "com.geovra.red");
      name = resources.getString(id);
    } catch (Exception e) {
      Log.d(TAG, e.getMessage());
    }
    return name;
  }


  public int getItemStatusPosition(AppCompatActivity ctx, int status)
  {
    List<Status> list = getItemStatusOptions(ctx);
    for (Status current : list) {
      if (current.getId() == status) {
        return list.indexOf(current);
      }
    }

    return 0;
  }


  public List<Status> getItemStatusOptions(AppCompatActivity ctx)
  {
    List<Status> options = new ArrayList<>();
    options.add(new Status(-1, "Choose status"));
    ArrayList<Integer> list = new ArrayList<Integer>() {{
      add(Item.STATUS_COMPLETED);
      add(Item.STATUS_PENDING);
      add(Item.STATUS_ADDED);
      add(Item.STATUS_POSTPONED);
      add(Item.STATUS_HUH);
      add(Item.STATUS_URGENT);
    }};

    for (int i : list) {
      try {
        int id = ctx.getResources().getIdentifier("status_" + String.valueOf(i), "string", "com.geovra.red");
        String name = ctx.getResources().getString(id);
        options.add(new Status(i, name));
      } catch (Exception e) {
        Log.d(TAG, e.toString());
      }
    }
    return options;
  }

}
