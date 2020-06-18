package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.geovra.red.R;
import com.geovra.red.app.adapter.DashboardPageAdapter;
import com.geovra.red.app.service.RedService;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.app.viewmodel.ViewModelSingletonFactory;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.filter.adapter.FilterPageAdapter;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.google.android.material.tabs.TabLayout;

public class FilterIndexActivity extends RedActivity {
  private static final String TAG = "FilterIndexActivity";
  public FilterPageAdapter adapter;
  public FilterViewModel vm;
  public RedService sRed;
  public TabLayout tabLayout;
  public ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_index);

    vm = ViewModelProviders.of(this, ViewModelSingletonFactory.getInstance()).get(FilterViewModel.class);
    findViewById(R.id.interval_switch).setVisibility(View.GONE);

    setViewPager();
  }

  public void setViewPager()
  {
    pager = (ViewPager) findViewById(R.id.viewPager);
    adapter = new FilterPageAdapter(
      getSupportFragmentManager(),
      this,
      vm );

    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    pager.setAdapter(adapter);
    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.setupWithViewPager(pager);
  }


}
