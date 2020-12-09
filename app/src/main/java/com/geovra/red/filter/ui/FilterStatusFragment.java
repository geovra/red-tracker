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
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.item.adapter.ItemRecyclerAdapter;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.geovra.red.shared.tab.TabTitle;

import java.util.ArrayList;

public class FilterStatusFragment extends Fragment implements TabTitle {
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
    SelectableRecyclerAdapter adapter = new SelectableRecyclerAdapter(getResources());
    recyclerView.setAdapter(adapter);
    adapter.changeDataSet(new ArrayList() {{
      add(new Status("Completed"));
      add(new Status("Postponed"));
      add(new Status("Huh"));
    }});

    return view;
  }


  public CharSequence getPageTitle() {
    return "Status";
  }
}
