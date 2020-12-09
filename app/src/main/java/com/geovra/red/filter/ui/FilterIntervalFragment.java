package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.geovra.red.app.ui.RedActivity;

import androidx.fragment.app.Fragment;
import com.geovra.red.databinding.FilterIntervalBinding;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.shared.date.DateService;
import com.geovra.red.shared.tab.TabTitle;

public class FilterIntervalFragment extends Fragment implements TabTitle {
  private RedActivity activity;
  private DateService dateService;
  private FilterViewModel vmFilter;

  public FilterIntervalFragment(FilterViewModel vmFilter) {
    this.vmFilter = vmFilter;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // View view = inflater.inflate(R.layout.filter_interval, container, false);
    FilterIntervalBinding binding = FilterIntervalBinding.inflate(inflater, container, false);
    binding.setIntervalFragment(this);
    binding.setViewModel(vmFilter);

    dateService = new DateService();

    dateService.setDialog(getActivity(), binding.selectFromBtn, (DatePicker view, int year, int month, int day, String result) -> {
      vmFilter.setDateFrom(result);
      binding.invalidateAll();
    });

    dateService.setDialog(getActivity(), binding.selectToBtn, (DatePicker view, int year, int month, int day, String result) -> {
      vmFilter.setDateTo(result);
      binding.invalidateAll();
    });

    return binding.getRoot();
  }


  public CharSequence getPageTitle() {
    return "Interval";
  }
}
