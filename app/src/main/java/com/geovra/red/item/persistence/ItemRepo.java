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
  private WeakReference<Context> ctx;
  public static final String TAG = "ItemService";
  public ItemApi api;
  public enum ACTION_TYPE { CREATE, READ, UPDATE, DELETE };

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  public ItemRepo(WeakReference<Context> ctx)
  {
    this.ctx = ctx;
    this.sRed = new RedService();
    this.prefs = new RedPrefs();
    if (ctx.get() != null) {
      this.api = RetrofitApi.create(ctx.get(), ItemApi.class, prefs);
    }
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
