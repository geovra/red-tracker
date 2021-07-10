package com.geovra.red.comment.persistence;

import android.annotation.SuppressLint;
import android.content.Context;

import com.geovra.red.app.http.RetrofitApi;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.app.service.RedService;
import com.geovra.red.comment.http.CommentResponse;
import com.geovra.red.comment.persistence.Comment;
import com.geovra.red.comment.http.CommentApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class CommentRepo extends RedService {
  public static final String TAG = "CommentRepo";
  private RedPrefs prefs;
  public CommentApi api;

  public CommentRepo(Context ctx)
  {
    this.prefs = new RedPrefs();
    this.api = RetrofitApi.create(ctx, CommentApi.class, prefs);
  }


  public Observable<Response<CommentResponse.CommentStore>> store(Context ctx, int idItem, String body)
  {
    return store(ctx, idItem, new Comment(body));
  }


  public Observable<Response<CommentResponse.CommentStore>> store(Context ctx, int idItem, Comment comment)
  {
    return api.storeComment(
      prefs.getString(ctx, "BEARER_TOKEN"),
      idItem,
      comment.getBody()
    ).subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());
  }
}
