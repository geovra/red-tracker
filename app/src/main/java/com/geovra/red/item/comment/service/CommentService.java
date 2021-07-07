package com.geovra.red.item.comment.service;

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
import com.geovra.red.item.comment.http.CommentApi;
import com.geovra.red.item.comment.http.CommentResponse;
import com.geovra.red.item.comment.http.CommentResponse.CommentStore;
import com.geovra.red.item.comment.persistence.Comment;
import com.geovra.red.item.http.ItemApi;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Complexity;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.status.persistence.Status;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class CommentService extends RedService {
  public static final String TAG = "CommentService";
  private RedPrefs prefs;
  public CommentApi api;


  public CommentService(Context ctx)
  {
    this.prefs = new RedPrefs();
    this.api = RetrofitApi.create(ctx, CommentApi.class, prefs);
  }


  public Observable<Response<CommentStore>> store(Context ctx, int idItem, String body)
  {
    return store(ctx, idItem, new Comment(body));
  }


  public Observable<Response<CommentStore>> store(Context ctx, int idItem, Comment comment)
  {
    return api.storeComment(
      prefs.getString(ctx, "BEARER_TOKEN"),
      idItem,
      comment.getBody()
    ).subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
  }
}
