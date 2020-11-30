package com.geovra.red.filter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.app.service.RedService;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.app.viewmodel.ViewModelSingletonFactory;
import com.geovra.red.filter.adapter.FilterPagerAdapter;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.google.android.material.tabs.TabLayout;

public class FilterIndexActivity extends RedActivity {
  private static final String TAG = "FilterIndexActivity";
  public FilterPagerAdapter pagerAdapter;
  public FilterViewModel vm;
  public RedService sRed;
  public TabLayout tabLayout;
  public ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_activity);

    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance()).get(FilterViewModel.class);

    findViewById(R.id.OVERLAY_TEST_INC).findViewById(R.id.filter_apply).setOnClickListener(view -> {
      Intent returnIntent = new Intent();
      returnIntent.putExtra("result", "{ dateFrom: 0000-00-00 }");
      setResult(1, returnIntent);
      finish();
    });

    setViewPager();
  }


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


  @Override
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }
}
