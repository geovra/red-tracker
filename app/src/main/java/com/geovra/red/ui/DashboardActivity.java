package com.geovra.red.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.DashboardPageAdapter;
import com.geovra.red.bus.Bus;
import com.geovra.red.bus.Event;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.http.item.ItemService;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.ItemEvent;
import com.geovra.red.ui.item.ItemCreateUpdateActivity;
import com.geovra.red.ui.item.ItemShowActivity;
import com.geovra.red.utils.DateUtils;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.geovra.red.RedService;
import com.geovra.red.viewmodel.ViewModelSingletonFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Function;
import lombok.SneakyThrows;
import retrofit2.Response;

/**
 * View class
 */
@SuppressWarnings("CheckResult")
public class DashboardActivity extends RedActivity {
  private static final String TAG = "DashboardActivity";
  public DashboardPageAdapter adapter;
  public DashboardViewModel vm;
  public RedService sRed;
  public TabLayout tabLayout;
  public ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dashboard_main);
    setToolbar(null);

    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance()).get(DashboardViewModel.class);
    // UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class); // In fragments

    // vm.readCookie()
    //   .doOnNext(status -> {
    //     String cookie = status.raw().request().header("Cookie");
    //     vm.setCookie(cookie);
    //     vm.readItems();
    //   })
    //   .subscribe();

    vm.getItemService().heartbeat(new Function<Response<ItemResponse.ItemStatus>, Void>() {
      @Override
      public Void apply(Response<ItemResponse.ItemStatus> res) throws Exception {
        String cookie = res.raw().request().header("Cookie");

        vm.setCookie(DashboardActivity.this, cookie);
        vm.readItems("w");

        Log.i(TAG, cookie +" "+ res.body().getData());
        return null;
      }
    });

    Bus.listen(getDisposable(), ItemEvent.Created.class, event -> {
      Log.d(TAG, event.toString());
    });

    // ItemCreateUpdateActivity
    if /** ... 500 */ (false) {
      Item item = new Item(); // ... 500
      item.setId(143);
      item.setTitle("Date property");
      item.setDescription("Items include a \"date\" field which represents the target date for a given object.\nOn Android it means a Datepicker object.");
      item.setStatus(1);
      Intent intent = new Intent(this, ItemCreateUpdateActivity.class);
      intent.putExtra("item", new Gson().toJson(item));
      intent.putExtra("_type", ItemService.ACTION_TYPE.UPDATE.toString());
      this.startActivity(intent);
    }


    // ItemShowActivity
    if /** ... 500 */ (false) {
      Gson gson = new Gson();
      Item item = new Item(); // ... 500
      item.setTitle("Choose firm for kitchen furniture");
      item.setDescription("a) Find firm \nb) Call them for an offer");
      item.setStatus(1);
      Intent intent = new Intent(this, ItemShowActivity.class);
      intent.putExtra("item", gson.toJson(item));
      this.startActivity(intent);
    }


    // vm.getItemsData().observe(this, new Observer<List<Item>>() {
    //   @Override
    //   public void onChanged(List<Item> items) {
    //     setViewPager();
    //   }
    // })

    setViewPager();

    // ...
  }


  public void doItemRemove()
  {
    // Item item = vm.readItem(6);
    Item item = new Item();
    item.setId(7);

    vm.getItemService().remove(item)
      .subscribe(
        res -> {
          Log.i(TAG, res.toString());
        },
        err -> {
          Log.e(TAG, err.toString());
        },
        () -> {
          Log.d(TAG, "doItemRemove/completed");
        }
      );
  }


  public void setViewPager()
  {
    pager = (ViewPager) findViewById(R.id.viewPager);
    adapter = new DashboardPageAdapter(
        getSupportFragmentManager(),
        this,
        vm,
        vm.getIntervalDays() );

    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    pager.setAdapter(adapter);
    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    // tabLayout.setupWithViewPager(pager);

    setTabListener();
    setTabCurrentDay();
  }


  /**
   * Purpose: Huh?!?
   */
  public void setTabListener()
  {
    final int tabTodayIndex = vm.getItemService().setTabs(LayoutInflater.from(this), tabLayout);
    final AppCompatActivity ctx = this;
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        Date date = vm.readDateByPosition(tab.getPosition(), "yyyy-MM-dd", "date");
        vm.getDDateCurrent().setValue(date);
        try {
          // TabLayout.Tab tabToday = tabLayout.getTabAt(tabTodayIndex);
          // tabToday.getCustomView().setVisibility(View.GONE);
          // View view = vm.getItemService().getTabCustomView( LayoutInflater.from(ctx), (String) tab.getTag(), R.layout.tab_main_day_today, tab );
          // tabToday.setCustomView(view);
        } catch (Exception e) {}
        pager.setCurrentItem(tab.getPosition());
        // vm.itemsViewableUpdate(date);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        try {
          // TabLayout.Tab tabToday = tabLayout.getTabAt(tabTodayIndex);
          // tabToday.getCustomView().setVisibility(View.GONE);
          // View view = sItem.getTabCustomView( LayoutInflater.from(ctx), (String) tab.getTag(), R.layout.tab_main_day_today, tab );
          // tabToday.setCustomView(view);
        } catch (Exception e) {}
        pager.setCurrentItem(tab.getPosition());
      }
    });
  }


  public void setTabCurrentDay()
  {
    int day = vm.getDayOfWeek() - 1;
    tabLayout.setScrollPosition(day,0f,true);
    vm.getDDateCurrent().setValue(vm.getTime());
    pager.setCurrentItem(day);
  }


  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // String m = vm.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.item_add:
        vm.getItemService().toCreate(this, ItemCreateUpdateActivity.class);
        break;
      // case R.id.item_search:
      //   sItem.toSearch(this, ItemFilterActivity.class);
      //   break;
      // case R.id.item_status_pending:
      //   sItem.toSearch(this, ItemFilterActivity.class);
      //   break;
    }

    // if (item.getItemId() == R.id.action_chat) {
    // ...
    // } else if (...) {...}

    return true;
  }


  @Override
  protected void onResume() {
    super.onResume();
    Bus.consume(this, Bus.EVENTS_REMOVE);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    Bus.dispose(disposable);
  }
}