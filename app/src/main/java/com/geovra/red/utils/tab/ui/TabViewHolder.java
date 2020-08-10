package com.geovra.red.utils.tab.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.ui.ItemShowActivity;
import com.google.gson.Gson;

import lombok.Getter;

public class TabViewHolder extends RecyclerView.ViewHolder {
  @Getter
  private int layoutId;
  @Getter
  private LayoutInflater inflater;
  public int viewType;
  public RecyclerView.ViewHolder holderOriginal;
  public OnBind onBind;

  public TabViewHolder(View view, int viewType)
  {
    super(view);
    this.viewType = viewType;
  }


  public TabViewHolder(int layoutId, LayoutInflater inflater, OnBind onBind)
  {
    super(new View(null));
    this.layoutId = layoutId;
    this.inflater = inflater;
    this.onBind = onBind;
  }


  public TabViewHolder setHolderOriginal(View view, int viewType)
  {
    this.holderOriginal = new BaseViewHolder(view);
    return this;
  }


  // e.g.
  //
  // Item item = entries.get(position);
  // holder.item_title.setText(item.getTitleReadable());
  // View view = holder.itemView;
  // ImageView img = view.findViewById(R.id.item_status);
  // Resources resources = activity.getResources();
  //
  // int color = resources.getColor(holder.viewType == 0 ? R.color.FF_00 : R.color.colorPrimaryMid);
  // view.setBackgroundColor(color);
  //
  // vmDashboard.getItemService().setItemStatus(img, resources, item.getStatus(), item.getComplexity());
  public void onBind(TabViewHolder holder, int position) {
    onBind.onBind(holder, position);
  }


  public class BaseViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "TabViewHolder.BaseViewHolder";

    public BaseViewHolder(View view) {
      super(view);
    }
  }


  public interface OnCreateViewHolder<T> {
    public T onCreateViewHolder(ViewGroup parent, int viewType);
  }


  public interface OnBind {
    public void onBind(TabViewHolder holder, int position);
  }
}
