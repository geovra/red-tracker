package com.geovra.red.ui.item;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.item.ItemAdapterBase;
import com.geovra.red.bus.Bus;
import com.geovra.red.bus.Event;
import com.geovra.red.databinding.ItemCreateBinding;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.http.item.ItemService;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.ItemEvent;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.geovra.red.viewmodel.ViewModelSingletonFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

@SuppressLint("CheckResult")
public class ItemCreateUpdateActivity extends RedActivity implements DatePickerDialog.OnDateSetListener  {
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

    try {
      Intent intent = this.getIntent();
      Gson gson = new Gson();
      model = gson.fromJson(
        intent.getStringExtra("item"),
        Item.class );
      model = (null != model) ? model : new Item(); // required
    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }
    // model = vm.getItemService().getItemFake(this);

    // setContentView(R.layout.item_show);
    binding = DataBindingUtil.setContentView(this, R.layout.item_create);
    binding.setModel(model);

    String _type_ext = getIntent().getStringExtra("_type");
    _type = _type_ext == null ? ItemService.ACTION_TYPE.CREATE : ItemService.ACTION_TYPE.valueOf(_type_ext);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    setToolbar(null);
    setSpinner();
    setFocusListeners();
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    itemCreate = binding.itemCreateFab;
    itemCreate.setColorFilter(Color.WHITE);

    switch (_type) {
      case UPDATE:
        itemCreate.setImageResource(R.drawable.ic_action_foursquare_white);
        break;
    }

    binding.nsvContent.setOnTouchListener(this::onTouch);

    binding.date.setOnClickListener(view -> {
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      DatePickerDialog dialog = new DatePickerDialog( this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, this,
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
      dialog.show();
    });

    binding.date.setOnFocusChangeListener((View v, boolean hasFocus) -> {
      if (! hasFocus) return;
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      DatePickerDialog dialog = new DatePickerDialog( this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, this,
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
      dialog.show();
    });

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

              Bus.replace(ItemResponse.ItemStore.class, new Event<ItemResponse.ItemStore>(res.body()));

              onBackPressed();
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

              ItemEvent.Updated updated = new ItemEvent.Updated( model );
              Bus.replace(ItemEvent.Updated.class, new Event<ItemEvent.Updated>(updated));

              onBackPressed();
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


  @Override
  public void onDateSet(DatePicker view, int year, int month, int day) {
    Log.d(TAG, String.format("%d %d %d", year, month, day));
  }

}

