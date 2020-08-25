package com.geovra.red.shared.tab.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.shared.tab.ui.TabViewHolder;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

@SuppressLint("CheckResult")
public class TabRecyclerAdapter<Entry, ViewHolder extends TabViewHolder> extends RecyclerView.Adapter<ViewHolder> {
  private static final String TAG = "ItemRecycleAdapter";

  private List<Entry> entries = new ArrayList<>();

  private LayoutInflater mInflater;
  private Activity activity;
  protected Context ctx;

  protected ViewHolder holder;

  public <T extends RedActivity> TabRecyclerAdapter(T activity)
  {
    this.activity = activity;
    this.ctx = activity.getApplicationContext();
    this.mInflater = LayoutInflater.from(activity.getApplicationContext());
  }


  public <T extends RedActivity> TabRecyclerAdapter(T activity, ViewHolder holder)
  {
    this(activity);
    this.holder = holder;
  }


  @SuppressWarnings("unchecked")
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) // inflates the row layout from xml when needed
  {
    boolean isOdd = viewType == 0;

    View view = holder.getInflater().inflate(
      holder.getLayoutId(), parent, false
    );

    // int color = activity.getResources().getColor(isOdd ? R.color.FF_00 : R.color.colorPrimaryMid);
    // view.setBackgroundColor(color);

    return (ViewHolder) holder.setHolderOriginal(view, viewType);
  }


  @SneakyThrows
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.onBind(holder, position);
  }


  @Override
  public int getItemCount() {
    return entries.size();
  }


  @Override
  public int getItemViewType(int position) {
    return position % 2;
  }


  @SuppressWarnings("unchecked")
  Entry getItem(int id)
  {
    return entries.get(id);
  }


  public void setData(List<Entry> entries)
  {
    this.entries = entries;
    notifyDataSetChanged();
  }

}
