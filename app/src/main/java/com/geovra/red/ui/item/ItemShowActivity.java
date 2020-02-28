package com.geovra.red.ui.item;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.databinding.ItemShowBinding;
import com.geovra.red.model.Item;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.google.gson.Gson;

import lombok.ToString;

public class ItemShowActivity extends RedActivity {
  public static final String TAG = "ItemShowActivity";
  public DashboardViewModel vm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // setContentView(R.layout.item_show);

    Item item = new Item();
    try {
      Intent intent = getIntent();
      Gson gson = new Gson();
      item = gson.fromJson(
          intent.getStringExtra("item"),
          Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    ItemShowBinding binding = DataBindingUtil.setContentView(this, R.layout.item_show);
    binding.setModel(item);

    vm = ViewModelProviders.of(this).get(DashboardViewModel.class);

    setToolbar(null);




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


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    String m = vm.onOptionsItemSelected(item); // ... 500
    Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    // if (item.getItemId() == R.id.action_chat) {
    // ...
    // } else if (...) {...}
    return true;
  }

}
