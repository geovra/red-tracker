package com.geovra.red.shared.list;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;

import java.util.ArrayList;
import java.util.List;

import static com.geovra.red.shared.list.SelectableRecyclerAdapter.SelectableViewHolder;
import static com.geovra.red.shared.list.SelectableRecyclerAdapter.ViewHolderInput;

public class SelectableRecyclerAdapter<D extends ViewHolderInput> extends RecyclerView.Adapter<SelectableViewHolder>
{
  protected List<D> data = new ArrayList<>();
  protected Resources resources;
  protected boolean isMultiSelect = false;
  protected OnItemClickListener onItemClickListener = null;
  protected Selectable selectable = null;

  /**
   * Pass resources object to display simple images on the left and right of the recycler item.
   *
   * @param resources
   */
  public SelectableRecyclerAdapter(Resources resources, @NonNull Selectable selectable)
  {
    this.resources = resources;
    this.selectable = selectable;
  }


  @NonNull
  @Override
  public SelectableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    return new SelectableViewHolder(inflateViewHolder(parent, viewType), viewType, resources, this::onSelectedCallback);
  }

  protected View inflateViewHolder(@NonNull ViewGroup parent, int viewType) {
    int id = R.layout.data_item_selectable;
    boolean isOdd = (viewType == 0);
    return LayoutInflater.from(parent.getContext()).inflate(id, parent, false);
  }

  @Override
  public void onBindViewHolder(@NonNull SelectableViewHolder holder, int position)
  {
    holder.bind((ViewHolderInput) data.get(position), position);
  }


  /**
   * Internal behavior when item is selected.
   *
   * @param holder
   * @param view
   * @param position
   * @param length
   */
  protected void onSelectedCallback(SelectableViewHolder holder, View view, int position, int length)
  {
    if (selectable == null) { return; }
    List<? extends ViewHolderInput> list = selectable.getSelectable();
    List<? extends ViewHolderInput> selected = selectable.getSelected();
    ViewHolderInput input = selectable.getSelectable().get(position);

    if (length > 0) {
      isMultiSelect = true;
    } else if (! isMultiSelect) {
      return;
    }

    // Update opacity for selected items
    if (! selected.contains(input) && isMultiSelect) {
      holder.itemView.setAlpha(0.5f);
      holder.selected.setVisibility(View.VISIBLE);
      selectable.onSelect(input);
    } else {
      holder.itemView.setAlpha(1.0f);
      holder.selected.setVisibility(View.INVISIBLE);
      selectable.onDeselect(input);
    }

    if (selected.size() == 0) {
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
    public ImageView imageRight;
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

      onBindLhs(input, position, imageLeft);
      onBindRhs(input, position, selected);
    }


    /**
     * Customize left hand side of the recycler row
     */
    public void onBindLhs(ViewHolderInput input, int position, ImageView lhs)
    {
    }


    /**
     * Customize right hand side of the recycler row
     */
    public void onBindRhs(ViewHolderInput input, int position, ImageView rhs)
    {
    }


    @Override
    public boolean onLongClick(View view) {
      listener.onClick(this, view, getAdapterPosition(), 1);
      return true;
    }
  }


  public interface OnItemClickListener
  {
    void onClick(SelectableViewHolder holder, View view, int position, int length);
  }


  public static interface Selectable
  {
    List<? extends ViewHolderInput> getSelected();
    List<? extends ViewHolderInput> getSelectable();
    void onSelect(ViewHolderInput input);
    void onDeselect(ViewHolderInput input);
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
