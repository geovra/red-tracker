package com.geovra.red.dashboard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.app.adapter.Adapter;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.app.adapter.DashboardPageAdapter;
import com.geovra.red.filter.persistence.FilterOutput;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.filter.ui.FilterIndexActivity;
import com.geovra.red.item.service.ItemService;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.item.ui.ItemCreateUpdateActivity;
import com.geovra.red.item.ui.ItemShowActivity;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.app.service.RedService;
import com.geovra.red.app.viewmodel.ViewModelSingletonFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import lombok.Getter;

/**
 * View class
 */
@SuppressWarnings("CheckResult")
public class DashboardActivity extends RedActivity {
  private static final String TAG = "DashboardActivity";
  public DashboardPageAdapter viewPagerAdapter;
  public DashboardViewModel vm;
  public RedService sRed;
  @Getter public TabLayout tabLayout;
  @Getter public ViewPager pager;
  public static int ACTIVITY_FILTER_CODE = 1;
  public static int ACTIVITY_REQUEST_CODE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dashboard_main);
    setToolbar(null);
    // AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    sRed = new RedService();
    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance(getApplication())).get(DashboardViewModel.class);

    vm.readItems(this, "w");

    Bus.listen(getDisposable(), ItemEvent.Created.class, event -> {
      Log.d(TAG, event.toString());
    });

    if /** ... 500 */ (0>1) {
      FilterOutput filterOutput = new FilterOutput();
      filterOutput.setDateFrom("2020-12-01");
      filterOutput.setDateTo("2020-12-13");
      vm.readItemsByInterval(this, filterOutput);
    }

    if /** ... 500 FilterIndexActivity */ (1>0) {
      Intent intent = new Intent(this, FilterIndexActivity.class);
      startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    if /** ... 500 ItemCreateUpdateActivity */ (0>1) {
      Intent intent = new Intent(this, ItemCreateUpdateActivity.class);
      // intent.putExtra("_type", ItemService.ACTION_TYPE.UPDATE.toString());

      Item item = new Item(); // ... 500
      item.setId(155);
      // item.setTitle("Add item");
      // item.setDescription("feat\nUpdate: only left to so item::create\nEvery item is characterized by a complexity attribute depicted using simple shapes:\n\nActivities involved: item::index, item::show, item::create.");
      // item.setStatus(9);
      // item.setIsContinuous("0");
      // item.setDate("2020-04-01");
      intent.putExtra("item", new Gson().toJson(item));
      this.startActivity(intent);
    }

    if /** ... 500 ItemShowActivity */ (0>1) {
      Gson gson = new Gson();
      Item item = new Item(); // ... 500
      item.setTitle("Choose firm for kitchen furniture");
      item.setDescription("a) Find firm \nb) Call them for an offer\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren\nLorem ipsum sit amet incopren");
      item.setStatus(7);
      item.setComplexity(8);
      item.setDate("2020-03-31");
      Intent intent = new Intent(this, ItemShowActivity.class);
      intent.putExtra("item", gson.toJson(item));
      this.startActivity(intent);
    }

    findViewById(R.id.OVERLAY).setVisibility(View.GONE);
    findViewById(R.id.OVERLAY).setOnClickListener((view) -> {
      view.setVisibility(View.GONE);
    });

    setIntentToFilterIndex(); // Go to filter interval

    setViewPager();
  }


  private void setIntentToFilterIndex()
  {
    findViewById(R.id.interval_switch).setOnClickListener(view -> {
      Intent intent = new Intent(this, FilterIndexActivity.class);
      startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    });
  }


  public void doItemRemove()
  {
    // Item item = vm.readItem(6);
    Item item = new Item();
    item.setId(7);

    vm.getItemService().remove(this, item)
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
    viewPagerAdapter = new DashboardPageAdapter(
        getSupportFragmentManager(),
        this,
        vm);

    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    pager.setAdapter(viewPagerAdapter);
    tabLayout.setupWithViewPager(pager);
    // pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    // Sync tab list
    setTabs(LayoutInflater.from(this));
    vm.getIntervalDays().observe(this, (ArrayList<String> strings) -> {
      viewPagerAdapter.notifyDataSetChanged();
      setTabs(LayoutInflater.from(this));
    });
  }


  /**
   * FilterIndexActivity -> DashboardActivity
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ACTIVITY_REQUEST_CODE) {

      if (resultCode == RESULT_OK) {
        FilterOutput filterOutput = new Gson().fromJson( data.getStringExtra("result"), FilterOutput.class);
        vm.readItemsByFilters(this, filterOutput);
      }

      if (resultCode == RESULT_CANCELED) {
        //
      }
    }
  }


  public int setTabs(LayoutInflater inflater)
  {
    int tabTodayIndex = -1;
    final String today = vm.getDateService().getToday();
    ArrayList<String> days = vm.getIntervalDays().getValue();

    for (int i = 0; i < days.size(); i++) {

      TabLayout.Tab tab = getTabLayout().getTabAt(i); // tabLayout.newTab();

      boolean isToday = today.equals(days.get(i));
      int resId = isToday ? R.layout.tab_main_day : R.layout.tab_main_day_0;
      if (isToday) { tabTodayIndex = i; }

      View view = getTabCustomView( inflater, days.get(i), resId, null );
      tab.setCustomView(view);
      tab.setTag(days.get(i));
      // tabLayout.addTab(tab);
    }

    setTabCurrentDay();

    return tabTodayIndex;
  }


  public View getTabCustomView(LayoutInflater inflater, String day /* dd-MM-YYYY */, int layoutId, TabLayout.Tab tab)
  {
    // final LayoutInflater inflater = LayoutInflater.from(ctx);
    View view = inflater.inflate(layoutId, null);
    Pair<String, String> info = getTabInformation(day);

    TextView txName = (TextView) view.findViewById(R.id.int_day_name);
    TextView txNum = (TextView) view.findViewById(R.id.int_day_num);
    txName.setText( info.first.toUpperCase() );
    txNum.setText( info.second );

    return view;
  }


  public Pair<String, String> getTabInformation(String date /* dd-MM-YYYY */)
  {
    String e = vm.getDateService().getDayOfWeek(date);
    String d = vm.getDateService().getDayOfMonth(date);
    return new Pair<>(e, d);
  }


  public void setTabCurrentDay()
  {
    int dayIndex = 0;
    String today = vm.getDateService().getToday();

    for (int i = 0; i < vm.getIntervalDays().getValue().size(); i++) {
      TabLayout.Tab tab = tabLayout.getTabAt(i);
      String date = tab.getTag().toString();

      if (date.equals(today)) {
        dayIndex = i;
        break;
      }
    }

    tabLayout.setScrollPosition(dayIndex,0f,true);
    pager.setCurrentItem(dayIndex);
  }


  // @Override
  // public boolean onPrepareOptionsMenu(Menu menu)
  // {
  //   // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
  //   getMenuInflater().inflate(R.menu.menu_main, menu);
  //   return true;
  // }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId()) {

      case R.id.item_add:
        vm.getItemService().toActivity(this, ItemCreateUpdateActivity.class);
        break;

      case R.id.item_filter:
        vm.getItemService().toActivity(this, FilterIndexActivity.class, ACTIVITY_REQUEST_CODE);
        break;
    }

    return true;
  }


  public int getOptionsMenu()
  {
    return R.menu.menu_main;
  }


  @Override
  protected void onResume() {
    super.onResume();
    Bus.consume(this, Bus.EVENTS_REMOVE);
  }
}
