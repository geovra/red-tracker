package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.item.adapter.ItemRecyclerAdapter;

import java.util.Date;

public class FilterIntervalFragment extends FilterFragment {
  // ...
  private RedActivity activity;

  public FilterIntervalFragment()
  {}


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.filter_index, container, false);

    return view;
  }
}

