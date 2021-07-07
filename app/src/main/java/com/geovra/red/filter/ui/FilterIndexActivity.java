package com.geovra.red.filter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.app.service.RedService;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.ui.adapter.FilterPagerAdapter;
import com.geovra.red.filter.persistence.FilterOutput;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.shared.menu.MenuMain;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class FilterIndexActivity extends RedActivity {
  private static final String TAG = "FilterIndexActivity";
  public FilterPagerAdapter pagerAdapter;
  public FilterViewModel vm;
  public RedService sRed;
  public TabLayout tabLayout;
  public ViewPager pager;
  public MenuMain menuMain;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_activity);
    setToolbar();

    vm = ViewModelProviders.of(this).get(FilterViewModel.class);
    vm.permanentFilterCheck(this);

    /** Finish activity */
    findViewById(R.id.OVERLAY_TEST_INC).findViewById(R.id.filter_apply).setOnClickListener(this::goBack);

    setViewPager();
  }


  /**
   * Consists of 3 fragments: interval, status, category
   */
  public void setViewPager()
  {
    pager = (ViewPager) findViewById(R.id.viewPager);
    pagerAdapter = new FilterPagerAdapter(getSupportFragmentManager(), this, vm);

    // Replace tab layout
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    ViewGroup tabParent = (ViewGroup) tabLayout.getParent();
    tabParent.removeView(tabLayout);
    tabLayout = (TabLayout) LayoutInflater.from(this).inflate(R.layout.tab_list_v1, null);
    tabParent.addView(tabLayout);

    pager.setAdapter(pagerAdapter);
    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setupWithViewPager(pager);
  }


  public void goBack(View view)
  {
    Intent intent = new Intent();

    FilterOutput filterOutput = new FilterOutput();
    filterOutput.setDateFrom(vm.getDateFrom());
    filterOutput.setDateTo(vm.getDateTo());
    filterOutput.setStatus(vm.getStatusSelected().getValue());
    filterOutput.setCategories(vm.getCategorySelected().getValue());

    intent.putExtra("result", new Gson().toJson(filterOutput));
    setResult(RESULT_OK, intent);

    finish();
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId()) {

      case R.id.filter_permanent_store:
        permanentFilterToggle();
        break;
    }

    return true;
  }


  /**
   * Show bottom sheet with two options: to create or destroy the permanent filters.
   * Creating new permanent filters means that their values are persisted are are applied automatically for all subsequent queries.
   */
  public void permanentFilterToggle()
  {
    BottomSheetDialogFragment bottomSheet = new FilterPermanentToggleBottomSheet();
    bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
  }


  @Override
  public int getOptionsMenu()
  {
    return R.menu.menu_filter;
  }

}
