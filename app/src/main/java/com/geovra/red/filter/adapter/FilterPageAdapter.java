package com.geovra.red.filter.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.geovra.red.app.adapter.CacheFragmentStatePagerAdapter;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.filter.ui.FilterCategoryFragment;
import com.geovra.red.filter.ui.FilterFragment;
import com.geovra.red.filter.ui.FilterIndexFragment;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.filter.ui.FilterStatusFragment;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.item.ui.ItemIndexFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilterPageAdapter extends CacheFragmentStatePagerAdapter {
  private static final String TAG = "ItemPageAdapter";
  private int tabNumber;

  private ArrayList<String> data;
  private RedActivity activity;

  private FilterViewModel vmFilter;

  enum POS {
    INTERVAL,
    CATEGORY,
    STATUS,
  };

  public FilterPageAdapter(FragmentManager fm, RedActivity activity, FilterViewModel vmFilter)
  {
    super(fm);
    this.activity = activity;
    this.tabNumber = POS.values().length;
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
    FilterFragment fragment = null;

    switch (POS.values()[position]) {
      case INTERVAL:
        fragment = new FilterIntervalFragment();
        break;

      case CATEGORY:
        fragment = new FilterCategoryFragment();
        break;

      case STATUS:
        fragment = new FilterStatusFragment();
        break;
    }

    return (Fragment) fragment;
  }


  @Override
  public int getCount() {
    return tabNumber;
  }

}
