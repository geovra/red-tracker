package com.geovra.red.item.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;
import com.geovra.red.databinding.ItemShowBinding;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.dashboard.ui.DashboardActivity;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
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

    setToolbar(null);

    vm.getItemService().setItemStatus(binding.statusImg, getResources(), item.getStatus(), item.getComplexity());
    vm.getItemService().setItemStatus(binding.statusText, getResources(), item);

    binding.itemCreateFabRIGHT.setOnClickListener(this::onItemEdit);

    Bus.listen(getDisposable(), ItemEvent.Updated.class, (Event<ItemEvent.Updated> event) -> {
      item = (Item) event.getPayload().item;
      binding.setModel(item);
      vm.getItemService().setItemStatus(binding.statusImg, this.getResources(), item.getStatus(), item.getComplexity());
      vm.getItemService().setItemStatus(binding.statusText, this.getResources(), item);
      // binding.btnEdit.setOnClickListener(ItemListener.OnUpdate.getInstance(this, item)); // Manually refresh the listener like in the 90's
    });

    // binding.bottomAppBar.performHide(); // Does not work
  }


  public void onItemEdit(View view) {
    Intent intent = new Intent(view.getContext(), ItemCreateUpdateActivity.class);
    intent.putExtra("_type", "UPDATE");
    intent.putExtra("item", new Gson().toJson(item));
    startActivity(intent);
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
    String m = vm.onOptionsItemSelected(mi); // ... 500
    // Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    if (mi.getItemId() == R.id.menu_item_delete) {
      vm.getItemService().remove(this, item)
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
