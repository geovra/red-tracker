package com.geovra.red.item.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.geovra.red.item.persistence.Status;
import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.SelectableViewHolder;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.ViewHolderInput;

@SuppressLint("CheckResult")
public class StatusRecyclerAdapter<D extends ViewHolderInput> extends SelectableRecyclerAdapter<Status>
{
  private static final String TAG = "StatusRecyclerAdapter";


  /**
   * Pass resources object to display simple images on the left and right of the recycler item.
   *
   * @param resources
   * @param selectable
   */
  public StatusRecyclerAdapter(Resources resources, @NonNull Selectable selectable)
  {
    super(resources, selectable);
  }


  @NonNull
  @Override
  public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
  {
    return new StatusViewHolder(inflateViewHolder(parent, viewType), viewType, resources, this::onSelectedCallback);
  }


  public static class StatusViewHolder extends SelectableViewHolder
  {
    public StatusViewHolder(View itemView, int viewType, Resources resources, OnItemClickListener listener)
    {
      super(itemView, viewType, resources, listener);
    }


    /**
     * Customize left hand side of the recycler row
     */
    @Override
    public void onBindLhs(ViewHolderInput input, int position, ImageView lhs)
    {
      lhs.setColorFilter(Color.parseColor(((Status) input).getColor()), PorterDuff.Mode.SRC_IN);
    }


    /**
     * Customize right hand side of the recycler row
     */
    @Override
    public void onBindRhs(ViewHolderInput input, int position, ImageView rhs)
    {
      ViewGroup.LayoutParams layoutParams = rhs.getLayoutParams();
      layoutParams.height = 15;
      layoutParams.width = 15;
      rhs.setLayoutParams(layoutParams);
      rhs.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }
  }
}
