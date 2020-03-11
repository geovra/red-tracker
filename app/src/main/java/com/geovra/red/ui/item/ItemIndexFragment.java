package com.geovra.red.ui.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.adapter.item.ItemRecycleAdapter;
import com.geovra.red.viewmodel.DashboardViewModel;

public class ItemIndexFragment extends Fragment {
  private RedActivity activity;
  private DashboardViewModel vmDashboard;
  private String input; // string => "MONDAY"

  public ItemIndexFragment(DashboardViewModel vmDashboard, String input /* , RedActivity activity */)
  {
    // this.activity = activity;
    this.input = input;
    this.vmDashboard = vmDashboard;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.data_item_list_basic, container, false);

    // Initialize recycler
    setRecyclerView(view);

    return view;
  }


  public void setRecyclerView(View view)
  {
    // RecyclerView setup
    RecyclerView recyclerView = view.findViewById(R.id.item_rv);

    recyclerView.setLayoutManager( new LinearLayoutManager(getContext()) );
    ItemRecycleAdapter adapter = new ItemRecycleAdapter( getActivity(), vmDashboard );
    recyclerView.setAdapter(adapter);

    // ItemRecycleAdapter adapter.setClickListener(this);
  }

}
