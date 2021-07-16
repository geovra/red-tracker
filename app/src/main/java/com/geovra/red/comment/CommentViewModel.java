package com.geovra.red.comment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.comment.http.CommentResponse;
import com.geovra.red.comment.http.CommentResponse.CommentRemove;
import com.geovra.red.comment.http.CommentResponse.CommentStore;
import com.geovra.red.comment.persistence.Comment;
import com.geovra.red.comment.persistence.CommentRepo;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.Getter;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class CommentViewModel extends RedViewModel {
  private static final String TAG = "CommentViewModel";
  private CommentRepo commentRepo;
  @Getter private MutableLiveData<Comment[]> commentList;

  public CommentViewModel(@NonNull Application application) {
    super(application);
  }


  public Observable<Response<CommentStore>> store(Context ctx, String comment, int itemId)
  {
    return commentRepo.store(ctx, itemId, comment)
      .map(response -> {
        Bus.replace(CommentStore.class, new Event<CommentStore>(comment));

        return response;
      })
      .doOnError(err -> {
        Log.d(TAG, String.format("%s %s", "CommentStore", err.toString()));
        err.printStackTrace();
      });
  }


  public Observable<Response<CommentRemove>> remove(Context ctx, Comment comment)
  {
    return commentRepo.remove(ctx, comment)
      .map(response -> {
        // Bus.replace(CommentEvent.Created.class, new Event<CommentEvent.Created>(
        //     new CommentEvent.Created(comment))
        // );

        return response;
      })
      .doOnError(err -> {
        Log.d(TAG, String.format("%s %s", "CommentRemove", err.toString()));
        err.printStackTrace();
      });
  }
}
