package com.geovra.red.ui.item;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.model.item.Status;
import com.geovra.red.utils.Toast;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
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
    binding.setActivity(this);

    String _type_ext = getIntent().getStringExtra("_type");
    _type = _type_ext == null ? ItemService.ACTION_TYPE.CREATE : ItemService.ACTION_TYPE.valueOf(_type_ext);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    setToolbar(null);
    setStatusSpinner();
    setFocusListeners();
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    setDateDialog();

    switch (_type) {
      case UPDATE:
        binding.itemCreateFab.setImageResource(R.drawable.ic_action_foursquare_white);
        break;
    }

    binding.itemCreateFab.setColorFilter(Color.WHITE);
    binding.btnDate.setColorFilter(Color.WHITE);

    binding.nsvContent.setOnTouchListener(this::onTouch);

    Disposable d = RxView.clicks(binding.itemCreateFab)
      .throttleFirst(1500, TimeUnit.MILLISECONDS)
      .subscribe(this::onTakeAction);

    // ...
  }


  public void setStatusSpinner()
  {
    AppCompatSpinner spinner = binding.itemSpinner;
    List<Status> options = vm.getItemService().getItemStatusOptions(this);
    ArrayAdapter<Status> spinnerAdapter = new ItemAdapterBase.StatusSpinnerAdapter(this, R.layout.item_modal_entry, options, vm.getItemService());
    spinner.setAdapter(spinnerAdapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override // Does not fire because...?
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // model.setStatus((int) view.getTag());
        // onItemSelected(parent, view, position, id);
        Status status = (Status) parent.getItemAtPosition(position);
        // int status = (int) view.findViewById(R.id.status).getTag();
        model.setStatus(status.getId());
        binding.invalidateAll();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });

    // spinner.setSelection(0);
  }


  public void setDateDialog()
  {
    DatePickerDialog.OnDateSetListener listener = (DatePicker view, int year, int month, int day) -> {
      Log.d(TAG, String.format("%d %d %d", year, month, day));
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.set(year, month, day);
      String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
      // binding.date.setText(date);
      model.setDate(date);
      binding.invalidateAll();
    };

    binding.btnDate.setOnClickListener(view -> { // Show date dialog
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      DatePickerDialog dialog = new DatePickerDialog( this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, listener,
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
      dialog.show();
    });
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
              Log.d(TAG, "CREATE " + res.toString());
              Toast.show(this, R.string.item_created, Toast.LENGTH_LONG);

              Bus.replace(ItemResponse.ItemStore.class, new Event<ItemResponse.ItemStore>(res.body()));

              onBackPressed();
            },
            err -> {
              Log.d(TAG, String.format("%s %s", "item::store", err.toString()));
              err.printStackTrace();
            },
            () -> {} );
        break;

      case UPDATE:
        vm.getItemService().update(model)
          .subscribe(
            res -> {
              Log.d(TAG, "UPDATE" + res.toString());
              Toast.show(this, R.string.item_updated, Toast.LENGTH_LONG);

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
  protected void onDestroy() {
    super.onDestroy();
  }

}

