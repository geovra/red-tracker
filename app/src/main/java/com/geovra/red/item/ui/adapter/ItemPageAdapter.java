package com.geovra.red.item.ui.adapter;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.app.adapter.CacheFragmentStatePagerAdapter;
import com.geovra.red.dashboard.ui.DashboardActivity;
import com.geovra.red.item.ui.ItemIndexFragment;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;

public class ItemPageAdapter extends CacheFragmentStatePagerAdapter {
  private static final String TAG = "ItemPageAdapter";
  private ArrayList<String> data;
  private RedActivity activity;
  private DashboardViewModel vmDashboard;

  public ItemPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard)
  {
    super(fm);
    this.activity = activity;
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
    String day = vmDashboard.getIntervalDays().getValue().get(position);

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

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return "-1";
  }

  @Override
  public int getCount() {
    return vmDashboard.getIntervalDays().getValue().size();
  }
}
