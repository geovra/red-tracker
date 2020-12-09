package com.geovra.red.shared.list;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.item.adapter.ItemRecyclerAdapter;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.ui.ItemShowActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SelectableRecyclerAdapter<D extends SelectableRecyclerAdapter.ViewHolderInput> extends RecyclerView.Adapter<SelectableRecyclerAdapter.SelectableViewHolder>
{
  private List<D> data = new ArrayList<>();
  private Resources resources;
  private boolean isMultiSelect = false;
  private List<D> selectedItems = new ArrayList<>();

  /**
   * Pass resources object to display simple images on the left and right of the recycler item.
   *
   * @param resources
   */
  public SelectableRecyclerAdapter(Resources resources)
  {
    this.resources = resources;
  }


  @NonNull
  @Override
  public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    int id = R.layout.data_item_selectable;
    boolean isOdd = (viewType == 0);
    View view = LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
    return new SelectableViewHolder(view, viewType, resources, this::onSelectedCallback);
  }


  @Override
  public void onBindViewHolder(@NonNull SelectableViewHolder holder, int position)
  {
    holder.bind((ViewHolderInput) data.get(position), position);
  }


  private void onSelectedCallback(SelectableViewHolder holder, View view, int position, int length)
  {
    D input = data.get(position);
    if (length > 0) {
      isMultiSelect = true;
    } else if (! isMultiSelect) {
      return;
    }

    // Update opacity for selected items
    if (! selectedItems.contains(input) && isMultiSelect) {
      holder.itemView.setAlpha(0.5f);
      holder.selected.setVisibility(View.VISIBLE);
      selectedItems.add(input);
    } else {
      holder.itemView.setAlpha(1.0f);
      selectedItems.remove(input);
      holder.selected.setVisibility(View.INVISIBLE);
    }

    if (selectedItems.size() == 0) {
      isMultiSelect = false;
    }
  }


  /**
   * Useful because the list items can change many times
   *
   * @param items
   */
  public void changeDataSet(List<D> items)
  {
    this.data = items;
    notifyDataSetChanged();
  }


  D getItem(int id)
  {
    return data.get(id);
  }


  @Override
  public int getItemCount()
  {
    return data.size();
  }


  @Override
  public int getItemViewType(int position)
  {
    return position % 2;
  }


  public static class SelectableViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
  {
    public int viewType;
    public Resources resources;
    public TextView title;
    public ImageView selected;
    public ImageView imageLeft;
    public OnItemClickListener listener;

    public SelectableViewHolder(View itemView, int viewType, Resources resources, OnItemClickListener listener)
    {
      super(itemView);
      this.viewType = viewType;
      this.listener = listener;
      this.resources = resources;

      title = (TextView) itemView.findViewById(R.id.input_title);
      selected = (ImageView) itemView.findViewById(R.id.input_selected);
      imageLeft = (ImageView) itemView.findViewById(R.id.input_left);
    }


    /**
     * Bind data to child views from holder
     *
     * @param input
     * @param position
     */
    public void bind(ViewHolderInput input, int position)
    {
      title.setText(input.getText());
      imageLeft.setColorFilter(resources.getColor(R.color.colorPrimaryLightest), PorterDuff.Mode.SRC_IN);

      itemView.setOnClickListener(view -> listener.onClick(this, view, getAdapterPosition(), 0)); // by lambda
      itemView.setOnLongClickListener(this); // by interface
    }


    @Override
    public boolean onLongClick(View view) {
      listener.onClick(this, view, getAdapterPosition(), 1);
      return true;
    }
  }


  public interface OnItemClickListener
  {
    public void onClick(SelectableViewHolder holder, View view, int position, int length);
  }


  public interface ViewHolderInput
  {
    /**
     * The text to show in the main TextView for any item in a RecyclerView
     * @return
     */
    String getText();
  }
}
