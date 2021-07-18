package com.geovra.red.item.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.CheckResult;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.comment.CommentViewModel;
import com.geovra.red.comment.persistence.Comment;
import com.geovra.red.item.ItemViewModel;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;
import com.geovra.red.databinding.ItemShowBinding;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.dashboard.DashboardViewModel;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;

import static com.geovra.red.item.persistence.ItemEvent.*;
import static com.geovra.red.item.ui.ItemCreateUpdateActivity.EVENT_TYPE;

public class ItemShowActivity extends RedActivity {
  public static final String TAG = "ItemShowActivity";
  public DashboardViewModel dashboardViewModel;
  private CommentViewModel commentViewModel;
  private ItemViewModel itemViewModel;
  private Item item;
  private Comment[] commentList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null
    setToolbar(null);

    dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

    ItemShowBinding binding = DataBindingUtil.setContentView(this, R.layout.item_show);

    setItemBinding(binding);
    setCommentBinding(binding);

    // binding.bottomAppBar.performHide(); // Does not work
  }


  private void setItemBinding(ItemShowBinding binding)
  {
    final Item item = readItemExtra();

    binding.setModel(item);

    itemViewModel.setItemStatus(binding.statusImg, getResources(), item.getStatus(), item.getComplexity());
    itemViewModel.setItemStatus(binding.statusText, getResources(), item);

    setOnItemCreate(binding);
    setOnItemUpdate(binding);
  }


  private Item readItemExtra()
  {
    try {
      Intent intent = getIntent();
      item = new Gson().fromJson( intent.getStringExtra("item"), Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }
    return (null != item) ? item : new Item();
  }


  private void setOnItemCreate(ItemShowBinding binding)
  {
    binding.itemCreateFabRIGHT.setOnClickListener(v -> {
      Intent intent = new Intent(this, ItemCreateUpdateActivity.class);
      intent.putExtra(EVENT_TYPE, "UPDATE");
      intent.putExtra("item", new Gson().toJson(item));
      startActivity(intent);
    });
  }


  private void setOnItemUpdate(ItemShowBinding binding)
  {
    Bus.listen(getDisposable(), Updated.class, (Event<Updated> event) -> {
      Item item = (Item) event.getPayload().item;
      binding.setModel(item);

      itemViewModel.setItemStatus(binding.statusImg, this.getResources(), item.getStatus(), item.getComplexity());
      itemViewModel.setItemStatus(binding.statusText, this.getResources(), item);

      // binding.btnEdit.setOnClickListener(ItemListener.OnUpdate.getInstance(this, item)); // Manually refresh the listener like in the 90's
    });
  }


  @SuppressLint("CheckResult")
  private void setCommentBinding(ItemShowBinding binding)
  {
    readCommentExtra();
    setOnCommentCreate(binding);
  }


  private void readCommentExtra()
  {
    Comment[] commentList = new Comment[]{};
    try {
      Intent intent = getIntent();
      commentList = new Gson().fromJson( intent.getStringExtra("comments"), Comment[].class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    // commentViewModel.getCommentList().postValue(commentList);
  }


  private void setOnCommentCreate(ItemShowBinding binding)
  {
    binding.commentStoreBtn.setOnClickListener(v -> {
      Disposable d = commentViewModel.store(this, binding.commentTextNew.getText().toString(),item.getId())
        .subscribe(res -> {
          Log.d(TAG, "CREATE " + res.toString());
          com.geovra.red.shared.Toast.show(this, R.string.comment_created, com.geovra.red.shared.Toast.LENGTH_LONG);
        });
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_item_show, menu);
    return true;
  }


  @SuppressLint("CheckResult")
  @Override
  public boolean onOptionsItemSelected(MenuItem mi)
  {
    String m = dashboardViewModel.onOptionsItemSelected(mi); // ... 500
    // Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    if (mi.getItemId() == R.id.menu_item_delete) {
      // vm.getItemRepo().remove(this, item)
      //   .subscribe(
      //     res -> {
      //       Log.i(TAG, res.toString());
      //       String title = res.body().getData().getTitle();
      //       Toast.makeText(this, "Deleted \"" + title + "\"", Toast.LENGTH_LONG).show();
      //
      //       Intent intent = new Intent(this, DashboardActivity.class);
      //       this.startActivity(intent);
      //     },
      //     err -> {
      //       Log.e(TAG, err.toString());
      //     },
      //     () -> {}
      //   );
    }

    return true;
  }


  @Override
  protected void onRestart() {
    super.onRestart();
    Bus.consume(this, Bus.EVENTS_KEEP);
  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    Bus.dispose(getDisposable());
  }

}
