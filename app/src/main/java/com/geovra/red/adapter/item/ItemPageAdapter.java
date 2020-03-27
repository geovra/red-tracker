package com.geovra.red.adapter.item;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.geovra.red.RedActivity;
import com.geovra.red.adapter.CacheFragmentStatePagerAdapter;
import com.geovra.red.ui.item.ItemIndexFragment;
import com.geovra.red.viewmodel.DashboardViewModel;

import java.util.ArrayList;

public class ItemPageAdapter extends CacheFragmentStatePagerAdapter {
  private int tabNumber;
  private ArrayList<String> data;
  private RedActivity activity;
  private DashboardViewModel vmDashboard;
  private ArrayList<String> intervalDays;

  public ItemPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard, ArrayList<String> intervalDays)
  {
    super(fm);
    this.activity = activity;
    this.tabNumber = intervalDays.size();
    this.intervalDays = intervalDays;
    this.vmDashboard = vmDashboard;
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
    String day = intervalDays.get(position);
    ItemIndexFragment fragment = new ItemIndexFragment(vmDashboard, day);
    return fragment;
  }


  @Override
  public int getCount() {
    return tabNumber;
  }

}
