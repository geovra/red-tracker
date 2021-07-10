package com.geovra.red.item.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.ui.adapter.ItemRecyclerAdapter;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.google.gson.Gson;

import java.util.Date;

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
    ItemRecyclerAdapter adapter = new ItemRecyclerAdapter( ((RedActivity) getActivity()).getDisposable(), vmDashboard, date, this::onClick, this::onLongClick);

    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    vmDashboard.getDItemsResponse().observe((LifecycleOwner) getActivity(), items -> {
      Log.d(TAG, items.toString());
      adapter.changeDataSet(
        vmDashboard.readViewableItems(items, date)
      );
    });
  }


  // Keep method here or move to ViewModel
  public void onClick(View view, Item item)
  {
    Log.d(TAG, "onClick");

    Intent intent = new Intent(getContext(), ItemShowActivity.class);

    Gson gson = new Gson();
    intent.putExtra("item", gson.toJson(item));

    activity.startActivity(intent);
  }


  // Keep method here or move to ViewModel
  public boolean onLongClick(View v, Item item)
  {
    // int position = this.getLayoutPosition();
    // Item item = items.get(position);

    // Toast.makeText(ctx, "item/deleting " + item.getTitle(), Toast.LENGTH_SHORT).show();
    // vmDashboard.getItemService().remove(item)
    //   .subscribe(
    //     res -> {
    //       Log.i(TAG, res.toString());
    //       Toast.makeText(ctx, "item/deleted", Toast.LENGTH_SHORT).show();
    //       ItemRecycleAdapter.this.items.remove(position);
    //       ItemRecycleAdapter.this.notifyDataSetChanged();
    //     },
    //     err -> {
    //       Log.e(TAG, err.toString());
    //     },
    //     () -> {
    //       Log.d(TAG, "doItemRemove/completed");
    //     }
    //   );

    return true;
  }

}
