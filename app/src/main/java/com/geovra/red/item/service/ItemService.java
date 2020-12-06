package com.geovra.red.item.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.R;
import com.geovra.red.app.service.RedService;
import com.geovra.red.item.http.ItemApi;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Complexity;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.app.persistence.RedPrefs;
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

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

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
        // Log the response string
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
            Log.w(TAG, String.format("[response] method:%s %s %s", request.method(), request.url().toString(), body) );

            ResponseBody rb = ResponseBody.create(response.body().contentType(), body);
            return response.newBuilder().body(rb).build();
          }
        })
        .build();

    api = new Retrofit.Builder()
        .baseUrl("http://geovra-tracker.herokuapp.com/") // .baseUrl("https://jsonplaceholder.typicode.com/")
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
    return api.getItemsByInterval(
        ItemApi.AUTHORIZATION_HEADER,
        interval,
        10 )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemStore>> store(Item item)
  {
    return api.storeItem(
        ItemApi.AUTHORIZATION_HEADER,
        item.getTitle(),
        item.getDescription(),
        item.getStatus(),
        item.getIsContinuous(),
        item.getComplexity(),
        item.getDate()
    ).subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemUpdate>> update(Item item)
  {
    return api.updateItem(
      ItemApi.AUTHORIZATION_HEADER,
      item.getId(),
      item.getTitle(),
      item.getDescription(),
      item.getStatus(),
      item.getIsContinuous(),
      item.getComplexity(),
      item.getDate(),
      "PUT" )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemRemove>> remove(Item item)
  {
    return api.removeItem(
      ItemApi.AUTHORIZATION_HEADER,
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


  public <T extends AppCompatActivity> void toActivity(T ctx, Class<?> cls)
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
    cookie = "Bearer 9CvU9jq23vvaDkYZa9Z3Pr7TN9x1CBNH00slMYcf";
    dCookie.setValue(cookie);
    RedPrefs.putString(activity, "COOKIE", cookie);
  }


  public Pair<Integer, Integer> setItemStatus(ImageView img, Resources resources, int status, int complexity)
  {
    int background = R.drawable.shape_circle;
    String sShape = "shape_circle";
    String sColor = "default";

    if (complexity == Item.COMPLEXITY_HARD) {
      background = R.drawable.shape_rect;
      sShape = "shape_rect";
    }

    if (complexity == Item.COMPLEXITY_FUCK) {
      background = R.drawable.shape_triangle;
      sShape = "shape_triangle";

      // ViewGroup.LayoutParams params = img.getLayoutParams();
      // float factor = resources.getDisplayMetrics().density;
      // params.width = (int) (28 * factor);
      // params.height = (int) (34 * factor);
      // img.setTranslationX(4 * factor);
      // img.setTranslationY(-12 * factor);
      // if (img.getId() == R.id.status_img) { // Hack alert, fuuuck!
      //   img.setTranslationX(-2 * factor);
      //   img.setTranslationY(-13 * factor);
      // }
      // img.setLayoutParams(params);
    }

    // Colors
    int color = R.color.yellowPrimary;
    if (true) {
      if (status == Item.STATUS_URGENT) {
        color = R.color.redPrimary;
        sColor = "redPrimary";
      }

      if (status == Item.STATUS_HUH) {
        color = R.color.bluePrimary;
        sColor = "bluePrimary";
      }

      if (status == Item.STATUS_POSTPONED) {
        color = R.color.tonePrimary;
        sColor = "tonePrimary";
      }

      if (status == Item.STATUS_ADDED) {
        color = R.color.greyDimmer;
        sColor = "greyDimmer";
      }

      if (status == Item.STATUS_COMPLETED) {
        color = R.color.greenPrimary;
        sColor = "greenPrimary";
      }
    }

    img.setImageDrawable(resources.getDrawable(background));
    // img.setBackground(resources.getDrawable(background));
    // img.setImageResource(background);
    img.setColorFilter(resources.getColor(color), PorterDuff.Mode.SRC_IN);

    // The second approach is always the best for this task. But still, if you want to go with the first approach then the correct way to use it is like this
    // int sdk = android.os.Build.VERSION.SDK_INT;
    // if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
    //   setBackgroundDrawable();
    // } else {
    //   setBackground();
    // }

    return new Pair<>(background, color);
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


  public int getItemComplexityPosition(AppCompatActivity ctx, int complexity)
  {
    List<Complexity> list = getItemComplexityOptions(ctx);
    for (Complexity current : list) {
      if (current.getId() == complexity) {
        return list.indexOf(current);
      }
    }

    return 0;
  }


  // 500 Hardcoded
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


  // 500 Hardcoded
  public List<Complexity> getItemComplexityOptions(AppCompatActivity ctx)
  {
    List<Complexity> options = new ArrayList<>();
    options.add(new Complexity(-1, "Choose complexity"));

    ArrayList<Integer> list = new ArrayList<Integer>() {{
      add(Item.COMPLEXITY_OK);
      add(Item.COMPLEXITY_DOABLE);
      add(Item.COMPLEXITY_FUCK);
      add(Item.COMPLEXITY_HARD);
    }};

    for (int i : list) {
      try {
        int id = ctx.getResources().getIdentifier("complexity_" + String.valueOf(i), "string", "com.geovra.red");
        String name = ctx.getResources().getString(id);
        options.add(new Complexity(i, name));
      } catch (Exception e) {
        Log.d(TAG, e.toString());
      }
    }

    return options;
  }

}
