package com.geovra.red.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.DashboardPageAdapter;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.model.Item;
import com.geovra.red.ui.item.ItemCreateActivity;
import com.geovra.red.ui.item.ItemShowActivity;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.geovra.red.RedService;
import com.geovra.red.http.item.ItemService;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.functions.Function;
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
  public ItemService sItem;
  public TabLayout tabLayout;
  public ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dashboard_main);
    setToolbar(null);

    vm = ViewModelProviders.of(this).get(DashboardViewModel.class);
    // UserModel userModel = new ViewModelProvider(requireActivity()).get(UserModel.class); // In fragments

    // vm.readCookie()
    //   .doOnNext(status -> {
    //     String cookie = status.raw().request().header("Cookie");
    //     vm.setCookie(cookie);
    //     vm.readItems();
    //   })
    //   .subscribe();

    sItem = (new ItemService());

    sItem.heartbeat(new Function<Response<ItemResponse.ItemStatus>, Void>() {
      @Override
      public Void apply(Response<ItemResponse.ItemStatus> res) throws Exception {
        String cookie = res.raw().request().header("Cookie");
        vm.setCookie(cookie);
        vm.readItems();
        Log.i(TAG, cookie +" "+ res.body().getData());
        return null;
      }
    });

    if /** ... 500 */ (false) {
      Gson gson = new Gson();
      Item item = new Item(); // ... 500
      item.setTitle("Choose firm for kitchen furniture");
      item.setDescription("a) Find firm \nb) Call them for an offer");
      item.setStatus("1:Admin, 2:WTF, 3:Home, 4:Don't");
      Intent intent = new Intent(this, ItemCreateActivity.class);
      intent.putExtra("item", gson.toJson(item));
      this.startActivity(intent);
    }

    if /** ... 500 */ (false) {
      Gson gson = new Gson();
      Item item = new Item(); // ... 500
      item.setTitle("Choose firm for kitchen furniture");
      item.setDescription("a) Find firm \nb) Call them for an offer");
      item.setStatus("1:Admin, 2:WTF, 3:Home, 4:Don't");
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
    final int tabTodayIndex = sItem.setTabs(LayoutInflater.from(this), tabLayout);
    final AppCompatActivity ctx = this;
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        try {
          // TabLayout.Tab tabToday = tabLayout.getTabAt(tabTodayIndex);
          // tabToday.getCustomView().setVisibility(View.GONE);
          // View view = sItem.getTabCustomView( LayoutInflater.from(ctx), (String) tab.getTag(), R.layout.tab_main_day_today, tab );
          // tabToday.setCustomView(view);
        } catch (Exception e) {}
        pager.setCurrentItem(tab.getPosition());
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
        sItem.toCreate(this, ItemCreateActivity.class);
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

}