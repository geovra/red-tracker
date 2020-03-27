package com.geovra.red.adapter.item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.bus.Bus;
import com.geovra.red.bus.Event;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.ItemEvent;
import com.geovra.red.persistence.RedPrefs;
import com.geovra.red.ui.item.ItemShowActivity;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemViewHolder> {
  public static final String TAG = "ItemRecycleAdapter";
  private DashboardViewModel vmDashboard;
  private LayoutInflater mInflater;
  private List<Item> items = new ArrayList<>();
  private Activity activity;
  protected Context ctx;


  public <T extends Activity> ItemRecycleAdapter(T activity, DashboardViewModel vmDashboard) // Data is passed into the constructor
  {
    this.activity = activity;
    this.mInflater = LayoutInflater.from(activity.getApplicationContext());
    this.vmDashboard = vmDashboard;
    this.ctx = activity.getApplicationContext();

    vmDashboard.getItemsData().observe((LifecycleOwner) activity, new Observer<List<Item>>() {
      @Override
      public void onChanged(List<Item> items) {
        Log.d(TAG, items.toString());
        setData(items);
      }
    });

    Bus.listen(ItemResponse.ItemStore.class, (Event<ItemResponse.ItemStore> stored) -> {
      items.add(stored.getPayload().getData());
      notifyDataSetChanged();
      // stored.isDone();
      Log.d(TAG, (stored.getPayload()).toString());
    });

    Bus.listen(ItemEvent.Updated.class, (Event<ItemEvent.Updated> source) -> {
      for (int i = 0; i < items.size(); i++) {
        Item payload = source.getPayload().item;
        if (items.get(i).getId() != payload.getId()) { continue; }
        items.set(i, payload);
      }
      // synchronized (vmDashboard.getItemsData()) {
      //   // vmDashboard.getItemsData().notify();
      // }
      notifyDataSetChanged();
      Log.d(TAG, ((ItemEvent.Updated) source.getPayload()).toString());
    });

    Bus.listen(ItemEvent.Deleted.class, (Event<ItemEvent.Deleted> event) -> {
      synchronized (vmDashboard.getItemsData()) {
        vmDashboard.getItemsData().notify();
      }
      notifyDataSetChanged();
      Log.d(TAG, ((ItemEvent.Deleted) event.getPayload()).toString());
    });
  }


  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) // inflates the row layout from xml when needed
  {
    View view = mInflater.inflate(
        viewType == 0 ? R.layout.data_item_basic : R.layout.data_item_basic_secondary,
        parent,
        false);
    return new ItemViewHolder(view);
  }


  @Override
  public void onBindViewHolder(ItemViewHolder holder, int position) {
    Item item = items.get(position);
    holder.item_title.setText(item.getTitle());
  }

  @Override
  public int getItemCount() {
    return items.size();
  }


  @Override
  public int getItemViewType(int position) {
    return position % 2;
  }


  // Stores and recycles views as they are scrolled off screen
  public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public static final String TAG = "ViewHolder";
    TextView item_title;

    ItemViewHolder(View view) {
      super(view);
      item_title = view.findViewById(R.id.item_title);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this::onLongClick);
    }


    @Override
    public void onClick(View view) {
      Log.d(TAG, "onClick");
      Item item = items.get(this.getLayoutPosition());
      Gson gson = new Gson();

      Intent intent = new Intent(ctx, ItemShowActivity.class);
      intent.putExtra("item", gson.toJson(item));
      RedPrefs.putString(activity, "COOKIE", vmDashboard.getItemService().getCookie());

      activity.startActivity(intent);
    }


    @Override
    public boolean onLongClick(View v) {
      int position = this.getLayoutPosition();
      Item item = items.get(position);
      Toast.makeText(ctx, "item/deleting " + item.getTitle(), Toast.LENGTH_SHORT).show();
      vmDashboard.getItemService().remove(item)
        .subscribe(
          res -> {
            Log.i(TAG, res.toString());
            Toast.makeText(ctx, "item/deleted", Toast.LENGTH_SHORT).show();
            ItemRecycleAdapter.this.items.remove(position);
            ItemRecycleAdapter.this.notifyDataSetChanged();
          },
          err -> {
            Log.e(TAG, err.toString());
          },
          () -> {
            Log.d(TAG, "doItemRemove/completed");
          }
        );

      return true;
    }
  }


  Item getItem(int id) // convenience method for getting data at click position
  {
    return items.get(id);
  }


  public void setData(List<Item> items)
  {
    this.items = items;
    notifyDataSetChanged();
  }

}