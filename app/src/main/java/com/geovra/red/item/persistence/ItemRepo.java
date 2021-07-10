package com.geovra.red.item.persistence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.geovra.red.R;
import com.geovra.red.app.http.RetrofitApi;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.app.service.RedService;
import com.geovra.red.category.persistence.Category;
import com.geovra.red.item.http.ItemApi;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Complexity;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.shared.menu.MenuService;
import com.geovra.red.status.persistence.Status;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class ItemRepo extends RedService {
  public RedService sRed;
  private RedPrefs prefs;
  private WeakReference<AppCompatActivity> ctx;
  public static final String TAG = "ItemService";
  public ItemApi api;
  public enum ACTION_TYPE { CREATE, READ, UPDATE, DELETE };

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  public ItemRepo(Context ctx)
  {
    this.sRed = new RedService();
    this.prefs = new RedPrefs();
    this.api = RetrofitApi.create(ctx, ItemApi.class, prefs);
  }


  public <T extends AppCompatActivity> ItemRepo(T ctx) {
    this.ctx = new WeakReference<>(ctx);
  }


  public Observable<Response<ItemResponse.ItemIndex>> findAll(Context ctx, String interval, List<Status> statusList, List<Category> categoryList)
  {
    String status = "";
    if (null != statusList) for (Status s : statusList) { status += s.id + ","; } // Bro, do you even stream? API>19

    String category = "";
    if (null != categoryList) for (Category c : categoryList) { category += c.id + ","; }

    return api.getItemsByInterval(
        prefs.getString(ctx, "BEARER_TOKEN"),
        interval,
        status,
        category,
        10
      )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemStore>> store(Context ctx, Item item)
  {
    return api.storeItem(
        prefs.getString(ctx, "BEARER_TOKEN"),
        item.getTitle(),
        item.getDescription(),
        item.getStatus(),
        item.getIsContinuous(),
        item.getComplexity(),
        item.getDate()
    ).subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
  }


  public Observable<Response<ItemResponse.ItemUpdate>> update(Context ctx, Item item)
  {
    return api.updateItem(
      prefs.getString(ctx , "BEARER_TOKEN"),
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


  public Observable<Response<ItemResponse.ItemRemove>> remove(Context ctx, Item item)
  {
    return api.removeItem(
      prefs.getString(ctx, "BEARER_TOKEN"),
      item.getId(),
      "DELETE")
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
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