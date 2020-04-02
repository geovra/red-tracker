package com.geovra.red.ui.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.bus.Bus;
import com.geovra.red.bus.Event;
import com.geovra.red.databinding.ItemShowBinding;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.ItemEvent;
import com.geovra.red.persistence.RedPrefs;
import com.geovra.red.ui.DashboardActivity;
import com.geovra.red.utils.DateUtils;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.google.gson.Gson;

public class ItemShowActivity extends RedActivity {
  public static final String TAG = "ItemShowActivity";
  public DashboardViewModel vm;
  private Item item;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.item_show);

    try {
      Intent intent = getIntent();
      Gson gson = new Gson();
      item = gson.fromJson( intent.getStringExtra("item"), Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }
    item = (null != item) ? item : new Item();

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    ItemShowBinding binding = DataBindingUtil.setContentView(this, R.layout.item_show);
    binding.setModel(item);
    binding.setCtx(getApplicationContext());

    vm = ViewModelProviders.of(this).get(DashboardViewModel.class);
    String cookie = RedPrefs.getString(this, "COOKIE", null);
    vm.setCookie( this, cookie );

    setToolbar(null);

    vm.getItemService().setItemStatus(binding.statusImg, getResources(), item.getStatus(), item.getComplexity());
    vm.getItemService().setItemStatus(binding.statusText, getResources(), item);

    binding.btnEdit.setOnClickListener(ItemListener.OnUpdate.getInstance(this, item));

    Bus.listen(getDisposable(), ItemEvent.Updated.class, (Event<ItemEvent.Updated> event) -> {
      item = (Item) event.getPayload().item;
      binding.setModel(item);
      vm.getItemService().setItemStatus(binding.statusImg, this.getResources(), item.getStatus(), item.getComplexity());
      vm.getItemService().setItemStatus(binding.statusText, this.getResources(), item);
      binding.btnEdit.setOnClickListener(ItemListener.OnUpdate.getInstance(this, item)); // Manually refresh the listener like in the 90's
    });

    // final AnimationDrawable drawable = new AnimationDrawable();
    // final Handler handler = new Handler();
    // drawable.addFrame(new ColorDrawable(Color.RED), 1000);
    // drawable.addFrame(new ColorDrawable(Color.YELLOW), 1000);
    // drawable.setEnterFadeDuration(2000);
    // drawable.setOneShot(false);
    // ImageButton btn = (ImageButton) binding.btnClap;
    // btn.setBackground(drawable);
    // handler.postDelayed(() -> {
    //   drawable.start();
    // }, 100);

    // ...
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_item_show, menu);

    // menu.clear();
    //
    // menu.add(0, 1, 1, "Option A");
    // MenuItem mi1 = menu.findItem(1);
    // mi1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    // mi1.setIcon(R.mipmap.ic_action_achievement_white);
    // mi1.setS
    // String[] pieces = ItemShowActivity.class.toString().split(".");
    // Log.d(TAG, pieces[(pieces.length - 1)]);
    // menu.add(0, 2, 2, "Option B");

    return true;
  }


  @SuppressLint("CheckResult")
  @Override
  public boolean onOptionsItemSelected(MenuItem mi)
  {
    String m = vm.onOptionsItemSelected(mi); // ... 500
    // Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    if (mi.getItemId() == R.id.menu_item_delete) {
      vm.getItemService().remove(item)
        .subscribe(
          res -> {
            Log.i(TAG, res.toString());
            String title = res.body().getData().getTitle();
            Toast.makeText(this, "Deleted \"" + title + "\"", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, DashboardActivity.class);
            this.startActivity(intent);
          },
          err -> {
            Log.e(TAG, err.toString());
          },
          () -> {}
        );
    }

    // if (item.getItemId() == R.id.action_chat) {
    // ...
    // } else if (...) {...}
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
