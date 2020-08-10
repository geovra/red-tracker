package com.geovra.red.filter.ui;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.utils.tab.ui.TabFragment;

import java.util.Date;

public class FilterIndexFragment extends TabFragment {
  private RedActivity activity;
  private FilterViewModel vmDashboard;
  private Date date; // string => "MONDAY"

  protected int layoutFile = R.layout.data_item_list_basic;

  public FilterIndexFragment(FilterViewModel vmDashboard, Date date /* , RedActivity activity */)
  {
    // this.activity = activity;
    this.vmDashboard = vmDashboard;
    this.date = date;
  }


  public void setRecyclerView(View view, Date date)
  {
    // RecyclerView setup
    RecyclerView recyclerView = view.findViewById(R.id.item_rv);

    recyclerView.setLayoutManager( new LinearLayoutManager(getContext()) );
    // ItemRecyclerAdapter adapter = new ItemRecyclerAdapter( (RedActivity) getActivity(), vmDashboard, date );
    // recyclerView.setAdapter(adapter);

    // ItemRecycleAdapter adapter.setClickListener(this);
  }

}
