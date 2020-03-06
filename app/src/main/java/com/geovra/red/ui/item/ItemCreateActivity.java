package com.geovra.red.ui.item;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.item.ItemAdapterBase;
import com.geovra.red.databinding.ItemCreateBinding;
import com.geovra.red.http.item.ItemService;
import com.geovra.red.model.Item;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

@SuppressLint("CheckResult")
public class ItemCreateActivity extends RedActivity {
  public static final String TAG = "ItemCreateActivity";
  public DashboardViewModel vm;
  private ItemCreateBinding binding;
  protected Item model;
  protected ItemService sItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    vm = ViewModelProviders.of(this).get(DashboardViewModel.class);

    sItem = (new ItemService());

    model = sItem.getItemFake(this);

    // setContentView(R.layout.item_show);
    binding = DataBindingUtil.setContentView(this, R.layout.item_create);
    binding.setModel(model);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    setToolbar(null);

    setSpinner();

    setFocusListeners();

    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    // binding.itemCreateFab.setOnClickListener(this::onStore); // Store new item
    Disposable d = RxView.clicks(binding.itemCreateFab)
        .throttleFirst(1500, TimeUnit.MILLISECONDS)
        .subscribe(value -> {
          this.onStore();
        });

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
    binding.nsvContent.setOnClickListener(this::onClick);
  }


  public void onClick(View v)
  {
    keyboardHide(v.getWindowToken(), this);
  }


  public void onStore()
  {
    vm.itemStore(model)
      .subscribe(
        res -> {
          Log.d(TAG, "item::store" + res.toString());
          Toast.makeText(this, "item/saved", Toast.LENGTH_LONG).show();
        },
        err -> {
          Log.d(TAG, err.toString());
          err.printStackTrace();
        },
        () -> {} );
  }
}

