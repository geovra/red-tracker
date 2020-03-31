package com.geovra.red.adapter.item;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.bus.Bus;
import com.geovra.red.bus.Event;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.model.item.Item;
import com.geovra.red.model.item.ItemEvent;
import com.geovra.red.persistence.RedPrefs;
import com.geovra.red.ui.item.ItemShowActivity;
import com.geovra.red.viewmodel.DashboardViewModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

@SuppressLint("CheckResult")
public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemViewHolder> {
  private static final String TAG = "ItemRecycleAdapter";
  private DashboardViewModel vmDashboard;
  private LayoutInflater mInflater;
  private List<Item> items = new ArrayList<>();
  private Activity activity;
  private Date date;
  protected Context ctx;

  public <T extends RedActivity> ItemRecycleAdapter(T activity, DashboardViewModel vmDashboard, Date date) // Data is passed into the constructor
  {
    this.activity = activity;
    this.mInflater = LayoutInflater.from(activity.getApplicationContext());
    this.vmDashboard = vmDashboard;
    this.date = date;
    this.ctx = activity.getApplicationContext();

    // vmDashboard.getDateCurrent().observe((LifecycleOwner) activity, (String date) -> {
    //   Log.d(TAG, date);
    //   List<Item> _items = new ArrayList<>();
    //   for (Item item : items) {
    //     if (item.getCreatedAt() == date) {
    //       _items.add(item);
    //     }
    //   }
    //   setData(_items);
    // });

    vmDashboard.getDItemsResponse().observe((LifecycleOwner) activity, new Observer<List<Item>>() {
      @Override
      public void onChanged(List<Item> items) {
        Log.d(TAG, items.toString());
        List<Item> viewable = vmDashboard.readViewableItems(items, date);
        setData(viewable);
      }
    });

    Bus.listen(activity.getDisposable(), ItemResponse.ItemStore.class, (Event<ItemResponse.ItemStore> stored) -> {
      Item item = stored.getPayload().getData();
      if (items.contains(item)) { return; }
      items.add(item);
      notifyDataSetChanged();
      Log.d(TAG, (stored.getPayload()).toString());
    });

    Bus.listen(activity.getDisposable(), ItemEvent.Updated.class, (Event<ItemEvent.Updated> source) -> {
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

    Bus.listen(activity.getDisposable(), ItemEvent.Deleted.class, (Event<ItemEvent.Deleted> event) -> {
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
    View view = mInflater.inflate(id, parent, false);
    // int color = activity.getResources().getColor(isOdd ? R.color.FF_00 : R.color.colorPrimaryMid);
    // view.setBackgroundColor(color);
    return new ItemViewHolder(view, viewType);
  }


  @SneakyThrows
  @Override
  public void onBindViewHolder(ItemViewHolder holder, int position) {
    Item item = items.get(position);
    holder.item_title.setText(item.getTitleReadable());
    View view = holder.itemView;
    ImageView img = (ImageView) view.findViewById(R.id.item_status);
    Resources resources = holder.itemView.getContext().getResources();

    // Color
    int color = activity.getResources().getColor(holder.viewType == 0 ? R.color.FF_00 : R.color.colorPrimaryMid);
    view.setBackgroundColor(color);

    vmDashboard.getItemService().setItemStatus(img, resources, item);
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
    public int viewType;
    TextView item_title;

    ItemViewHolder(View view, int viewType) {
      super(view);
      this.viewType = viewType;
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