package com.geovra.red.item.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.geovra.red.R;
import com.geovra.red.app.adapter.Adapter;
import com.geovra.red.item.persistence.Complexity;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.shared.DateService;
import com.geovra.red.shared.Toast;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;
import com.geovra.red.databinding.ItemCreateBinding;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.service.ItemService;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.app.viewmodel.ViewModelSingletonFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

import static com.geovra.red.app.adapter.Adapter.SpinnerAdapter;

@SuppressLint("CheckResult")
public class ItemCreateUpdateActivity extends RedActivity {
  public static final String TAG = "ICUActivity";
  public DashboardViewModel vm;
  private ItemCreateBinding binding;
  protected Item model;
  protected ItemService.ACTION_TYPE _type;
  protected FloatingActionButton itemCreate;
  private DateService dateService;

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance()).get(DashboardViewModel.class);
    dateService = new DateService();

    try {
      Intent intent = this.getIntent();
      Gson gson = new Gson();
      model = gson.fromJson(
        intent.getStringExtra("item"),
        Item.class );
      model = (null != model) ? model : new Item(); // required
    } catch (Exception e) {
      model = vm.getItemService().getItemFake(this);
      Log.e(TAG, e.toString());
    }

    setContentView(R.layout.item_show);
    binding = DataBindingUtil.setContentView(this, R.layout.item_create);
    binding.setModel(model);
    binding.setActivity(this);

    String _type_ext = getIntent().getStringExtra("_type");
    _type = _type_ext == null ? ItemService.ACTION_TYPE.CREATE : ItemService.ACTION_TYPE.valueOf(_type_ext);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null

    setToolbar(null);
    setStatusSpinner();
    setComplexitySpinner();
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
  }


  public void setStatusSpinner()
  {
    AppCompatSpinner spinner = binding.itemSpinner;
    List<Status> options = vm.getItemService().getItemStatusOptions(this);
    Adapter.SpinnerAdapter spinnerAdapter = new SpinnerAdapter<Status>(this, R.layout.item_modal_entry, options);
    spinner.setAdapter(spinnerAdapter);
    binding.setSpinner(spinnerAdapter);

    spinnerAdapter.setCustomViewCallback( (SpinnerAdapter.CustomViewCallback<Status>) (data, label, image, layoutId, position, convertView, parent) -> {
      Status status = data.get(position);
      vm.getItemService().setItemStatus(image, getResources(), status.getId(), 0);
    } );

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override // Does not fire because...?
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Status status = (Status) parent.getItemAtPosition(position);
        model.setStatus(status.getId()); // model.setStatus((int) view.getTag());
        binding.invalidateAll();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });

    // spinner.setSelection(0);
  }


  public void setComplexitySpinner()
  {
    AppCompatSpinner spinner = binding.itemComplexity;
    List<Complexity> options = vm.getItemService().getItemComplexityOptions(this);

    Adapter.SpinnerAdapter spinnerAdapter = new SpinnerAdapter<Complexity>(this, R.layout.item_modal_entry, options);
    spinner.setAdapter(spinnerAdapter);

    spinnerAdapter.setCustomViewCallback( (SpinnerAdapter.CustomViewCallback<Complexity>) (data, label, image, layoutId, position, convertView, parent) -> {
      Complexity complexity = data.get(position);
      vm.getItemService().setItemStatus(image, getResources(), Item.STATUS_ADDED, complexity.getId());
    });

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override // Does not fire because...?
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Complexity complexity = (Complexity) parent.getItemAtPosition(position);
        model.setComplexity(complexity.getId());
        binding.invalidateAll();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {}
    });
  }


  public void setDateDialog()
  {
    dateService.setDialog(this, binding.btnDate, (DatePicker view, int year, int month, int day, String result) -> {
      model.setDate(result);
      binding.invalidateAll();
    });

    // DatePickerDialog.OnDateSetListener listener = (DatePicker view, int year, int month, int day) -> {
    //   Log.d(TAG, String.format("%d %d %d", year, month, day));
    //   Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    //   calendar.set(year, month, day);
    //   String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    //   // binding.date.setText(date);
    //   model.setDate(date);
    //   binding.invalidateAll();
    // };
    //
    // binding.btnDate.setOnClickListener(view -> { // Show date dialog
    //   Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    //   DatePickerDialog dialog = new DatePickerDialog( this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, listener,
    //     calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
    //   dialog.show();
    // });
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

