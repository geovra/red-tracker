package com.geovra.red.ui.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.item.ItemAdapterBase;
import com.geovra.red.databinding.ItemCreateBinding;
import com.geovra.red.http.item.ItemService;
import com.geovra.red.model.Item;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.geovra.red.viewmodel.ViewModelSingletonFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

@SuppressLint("CheckResult")
public class ItemCreateUpdateActivity extends RedActivity {
  public static final String TAG = "ICUActivity";
  public DashboardViewModel vm;
  private ItemCreateBinding binding;
  protected Item model;
  protected ItemService.ACTION_TYPE _type;
  protected FloatingActionButton itemCreate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance()).get(DashboardViewModel.class);

    model = null;
    try {
      Intent intent = this.getIntent();
      Gson gson = new Gson();
      model = gson.fromJson(
        intent.getStringExtra("item"),
        Item.class );
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }
    model = vm.getItemService().getItemFake(this);

    // setContentView(R.layout.item_show);
    binding = DataBindingUtil.setContentView(this, R.layout.item_create);
    binding.setModel(model);

    String ext = getIntent().getStringExtra("_type");
    _type = ext == null ? ItemService.ACTION_TYPE.CREATE : ItemService.ACTION_TYPE.valueOf(ext);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    setToolbar(null);

    setSpinner();

    setFocusListeners();

    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    itemCreate = binding.itemCreateFab;

    switch (_type) {
      case UPDATE:
        // itemAdd.setImageDrawable(R.drawable.ic_action_achievement_white);
        itemCreate.setImageResource(R.drawable.ic_action_foursquare_white);
        break;
    }

    binding.nsvContent.setOnTouchListener(this::onTouch);

    itemCreate.setColorFilter(Color.WHITE);

    Disposable d = RxView.clicks(binding.itemCreateFab)
      .throttleFirst(1500, TimeUnit.MILLISECONDS)
      .subscribe(this::onTakeAction);

    // ...
  }


  public void setSpinner()
  {
    Spinner sp = (Spinner) findViewById(R.id.item_spinner);
    List<String> options = vm.getItemStatusOptions();
    ArrayAdapter<String> spinnerAdapter = new ItemAdapterBase.StatusSpinnerAdapter(this, R.layout.item_spinner_entry, options);
    sp.setAdapter(spinnerAdapter);
    sp.setSelection(1);
  }


  public void setFocusListeners()
  {
    binding.itemName.setOnFocusChangeListener(ItemListener.FocusChange.getListener(R.id.item_name, this));
    binding.itemDesc.setOnFocusChangeListener(ItemListener.FocusChange.getListener(R.id.item_desc, this));
  }

  public void onTakeAction(Object view)
  {
    switch (_type) {

      case CREATE:
        vm.getItemService().store(model)
          .subscribe(
            res -> {
              Log.d(TAG, "item::store" + res.toString());
              Toast.makeText(this, R.string.item_created, Toast.LENGTH_LONG).show();
            },
            err -> {
              Log.d(TAG, String.format("%s %s", "item::store", err.toString()));
              err.printStackTrace();
            },
            () -> {
            });
        break;

      case UPDATE:
        vm.getItemService().update(model)
          .subscribe(
            res -> {
              Log.d(TAG, "items/update" + res.toString());
              Toast.makeText(this, R.string.item_updated, Toast.LENGTH_LONG).show();
            },
            err -> {
              Log.d(TAG, String.format("%s %s", "item/update", err.toString()));
              err.printStackTrace();
            },
            () -> {
            });
        break;

    }
  }


  public boolean onTouch(View v, MotionEvent event) {
    keyboardHide(v.getWindowToken(), this);

    return true;
  }

}

