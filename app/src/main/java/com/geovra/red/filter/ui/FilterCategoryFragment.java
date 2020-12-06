package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.shared.tab.TabTitle;

public class FilterCategoryFragment extends Fragment implements TabTitle {
  private RedActivity activity;
  private FilterViewModel vmFilter;

  public FilterCategoryFragment(FilterViewModel vmFilter) {
    this.vmFilter = vmFilter;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // return inflater.inflate(R.layout.filter_interval, container, false);
    return null;
  }

  public CharSequence getPageTitle() {
    return "Category";
  }
}
