package com.geovra.red.adapter.item;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.geovra.red.RedActivity;
import com.geovra.red.adapter.CacheFragmentStatePagerAdapter;
import com.geovra.red.ui.item.ItemIndexFragment;
import com.geovra.red.viewmodel.DashboardViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ItemPageAdapter extends CacheFragmentStatePagerAdapter {
  private static final String TAG = "ItemPageAdapter";
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

    try {

      SimpleDateFormat format = new SimpleDateFormat(DashboardViewModel.PAT_YY_MM_DD);
      Date date = format.parse(day);
      ItemIndexFragment fragment = new ItemIndexFragment(vmDashboard, date);
      return fragment;

    } catch (Exception e) {
      Log.e(TAG, e.toString());
    }

    return null;
  }


  @Override
  public int getCount() {
    return tabNumber;
  }

}
