package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.databinding.ItemShowBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.geovra.red.databinding.FilterIntervalBinding;
import com.geovra.red.shared.DateService;
import com.geovra.red.shared.tab.TabTitle;

public class FilterIntervalFragment extends Fragment implements TabTitle {
  private RedActivity activity;
  private DateService dateService;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // View view = inflater.inflate(R.layout.filter_interval, container, false);
    FilterIntervalBinding binding = FilterIntervalBinding.inflate(inflater, container, false);
    binding.setIntervalFragment(this);

    dateService = new DateService();

    dateService.setDialog(getActivity(), binding.selectFromBtn, (DatePicker view, int year, int month, int day, String result) -> {
      // model.setDate(date);
      // binding.invalidateAll();
    });

    dateService.setDialog(getActivity(), binding.selectToBtn, (DatePicker view, int year, int month, int day, String result) -> {
      // model.setDate(date);
      // binding.invalidateAll();
    });

    return binding.getRoot();
  }


  public CharSequence getPageTitle() {
    return "Interval";
  }
}
