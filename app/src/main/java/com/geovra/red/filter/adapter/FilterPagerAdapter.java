package com.geovra.red.filter.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.geovra.red.app.adapter.CacheFragmentStatePagerAdapter;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.ui.FilterCategoryFragment;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.filter.ui.FilterStatusFragment;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.shared.tab.TabTitle;

import java.util.ArrayList;

public class FilterPagerAdapter extends CacheFragmentStatePagerAdapter {
  private static final String TAG = "ItemPageAdapter";
  private int tabNumber;

  private ArrayList<String> data;
  private RedActivity activity;

  private FilterViewModel vmFilter;

  enum Position {
    INTERVAL,
    CATEGORY,
    STATUS,
  };

  public FilterPagerAdapter(FragmentManager fm, RedActivity activity, FilterViewModel vmFilter)
  {
    super(fm);
    this.activity = activity;
    this.tabNumber = Position.values().length;
    this.vmFilter = vmFilter;
  }


  @NonNull
  @Override
  public Fragment getItem(int position)
  {
    Fragment fragment = createItem(position);
    return fragment;
  }


  public Fragment createItem(int position)
  {
    return vmFilter.getMPages().get(position); // 500
  }


  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    String title = (String) ((TabTitle) vmFilter.getMPages().get(position)).getPageTitle();
    return title.substring(0, 1);
  }


  @Override
  public int getCount() {
    return tabNumber;
  }

}
