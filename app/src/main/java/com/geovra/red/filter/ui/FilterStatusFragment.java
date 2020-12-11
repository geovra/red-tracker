package com.geovra.red.filter.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.item.adapter.ItemRecyclerAdapter;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.geovra.red.shared.tab.TabTitle;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CheckResult")
public class FilterStatusFragment extends Fragment implements TabTitle {
  private static final String TAG = "FilterStatusFragment";
  private RedActivity activity;
  private FilterViewModel vmFilter;

  public FilterStatusFragment(FilterViewModel vmFilter) {
    this.vmFilter = vmFilter;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.filter_status, container, false);

    RecyclerView recyclerView = view.findViewById(R.id.status_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    SelectableRecyclerAdapter<Status> adapter = new SelectableRecyclerAdapter<>(getResources());
    recyclerView.setAdapter(adapter);

    vmFilter.getStatusService().findAll(getContext())
      .subscribe(
        res -> {
          List<Status> data = res.body().getData();
          Log.d(TAG, data.toString());
          adapter.changeDataSet(data);
        },
        error -> {
          Log.d(TAG, error.toString());
          error.printStackTrace();
        },
        () -> Log.d(TAG, "200 readItems")
      );

    return view;
  }


  public CharSequence getPageTitle() {
    return "Status";
  }
}
