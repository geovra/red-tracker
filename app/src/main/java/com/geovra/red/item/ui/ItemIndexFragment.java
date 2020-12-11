package com.geovra.red.item.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.adapter.ItemRecyclerAdapter;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.item.persistence.Item;

import java.util.Date;
import java.util.List;

public class ItemIndexFragment extends Fragment {
  private static final String TAG = "ItemIndexFragment";
  private RedActivity activity;
  private DashboardViewModel vmDashboard;
  private Date date; // string => "MONDAY"

  public ItemIndexFragment(DashboardViewModel vmDashboard, Date date /* , RedActivity activity */)
  {
    // this.activity = activity;
    this.vmDashboard = vmDashboard;
    this.date = date;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.data_item_list_basic, container, false);

    // Initialize recycler
    setRecyclerView(view, date);

    return view;
  }


  public void setRecyclerView(View view, Date date)
  {
    RecyclerView recyclerView = view.findViewById(R.id.item_rv);
    ItemRecyclerAdapter adapter = new ItemRecyclerAdapter( (RedActivity) getActivity(), vmDashboard, date );

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    vmDashboard.getDItemsResponse().observe((LifecycleOwner) getActivity(), items -> {
      Log.d(TAG, items.toString());
      adapter.changeDataSet(
        vmDashboard.readViewableItems(items, date)
      );
    });
  }

}
