package com.geovra.red.item.ui.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.ItemViewModel;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;
import com.geovra.red.item.http.ItemResponse;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.dashboard.DashboardViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

@SuppressLint("CheckResult")
public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder> {
  private static final String TAG = "ItemRecycleAdapter";
  private DashboardViewModel vmDashboard;
  private List<Item> items = new ArrayList<>();
  private final OnClickListener onClickListener;
  private final OnLongClickListener onLongClickListener;
  private ItemViewModel itemViewModel;
  private Date date;

  public <T extends RedActivity> ItemRecyclerAdapter(Bus.Disposable disposable, DashboardViewModel vmDashboard, ItemViewModel itemViewModel, Date date, OnClickListener onClickListener, OnLongClickListener onLongClickListener) // Data is passed into the constructor
  {
    this.vmDashboard = vmDashboard;
    this.itemViewModel = itemViewModel;
    this.date = date;
    this.onClickListener = onClickListener;
    this.onLongClickListener = onLongClickListener;

    Bus.listen(disposable, ItemResponse.ItemStore.class, (Event<ItemResponse.ItemStore> stored) -> {
      Item item = stored.getPayload().getData();
      if (items.contains(item)) { return; }
      items.add(item);
      notifyDataSetChanged();
      Log.d(TAG, (stored.getPayload()).toString());
    });

    Bus.listen(disposable, ItemEvent.Updated.class, (Event<ItemEvent.Updated> source) -> {
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

    Bus.listen(disposable, ItemEvent.Deleted.class, (Event<ItemEvent.Deleted> event) -> {
      synchronized (vmDashboard.getDItems()) {
        vmDashboard.getDItems().notify();
      }
      notifyDataSetChanged();
      Log.d(TAG, ((ItemEvent.Deleted) event.getPayload()).toString());
    });
  }


  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) // inflates the row layout from xml when needed
  {
    boolean isOdd = viewType == 0;
    int id = R.layout.data_item_basic; // : R.layout.data_item_basic_secondary;
    View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
    // int color = activity.getResources().getColor(isOdd ? R.color.FF_00 : R.color.colorPrimaryMid);
    // view.setBackgroundColor(color);
    return new ItemViewHolder(view, viewType, onClickListener, onLongClickListener);
  }


  @SneakyThrows
  @Override
  public void onBindViewHolder(ItemViewHolder holder, int position) {
    Item item = items.get(position);
    holder.item_title.setText(item.getTitleReadable());
    View view = holder.itemView;
    ImageView img = view.findViewById(R.id.item_status);

    Resources resources = holder.itemView.getContext().getResources();
    int color = resources.getColor(holder.viewType == 0 ? R.color.FF_00 : R.color.colorPrimaryMid);
    view.setBackgroundColor(color);

    itemViewModel.setItemStatus(img, resources, item.getStatus(), item.getComplexity());
  }


  Item getItem(int id) // convenience method for getting data at click position
  {
    return items.get(id);
  }


  @Override
  public int getItemCount() {
    return items.size();
  }


  @Override
  public int getItemViewType(int position) {
    return position % 2;
  }


  /**
   * Constructor injection is not available because the items can change many times
   * @param items
   */
  public void changeDataSet(List<Item> items)
  {
    this.items = items;
    notifyDataSetChanged();
  }


  // Stores and recycles views as they are scrolled off screen
  public class ItemViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "ViewHolder";
    public int viewType;
    TextView item_title;

    ItemViewHolder(View view, int viewType, OnClickListener onClickListener, OnLongClickListener onLongClickListener) {
      super(view);
      this.viewType = viewType;
      item_title = view.findViewById(R.id.item_title);

      view.setOnClickListener(v -> {
        Item item = items.get(getLayoutPosition());
        onClickListener.onClick(v, item);
      });

      view.setOnLongClickListener(v -> {
        int position = this.getLayoutPosition();
        Item item = items.get(position);
        onLongClickListener.onLongClick(v, item);
        return false;
      });
    }
  }


  public interface OnClickListener {
    public void onClick(View view, Item item);
  }


  public interface OnLongClickListener {
    public void onLongClick(View view, Item item);
  }
}
