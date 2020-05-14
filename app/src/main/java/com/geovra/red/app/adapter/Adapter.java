package com.geovra.red.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geovra.red.R;
import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Adapter {

  // item::spinner
  public static class SpinnerAdapter<T> extends ArrayAdapter<T> {
    private List<T> objects;
    private Context context;
    @Getter @Setter
    private CustomViewCallback customViewCallback;

    public SpinnerAdapter(Context context, int resourceId, List<T> objects) {
      super(context, resourceId, objects);
      this.objects = objects;
      this.context = context;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
      return getCustomView(R.layout.item_modal_entry, position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return getCustomView(R.layout.item_modal_entry_light, position, convertView, parent);
    }


    public View getCustomView(int layoutId, int position, View convertView, ViewGroup parent)
    {
      LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View row = inflater.inflate(layoutId, parent, false);
      // Status status = objects.get(position);

      TextView label = (TextView) row.findViewById(R.id.status_text);
      ImageView image = row.findViewById(R.id.status_img);

      String original = new Gson().toJson(objects.get(position)); // Dirty hack
      SpinnerData input = new Gson().fromJson(original, SpinnerData.class);
      label.setText(input.getName());
      label.setTag(input.getId());

      // Pair<Drawable, Integer> pair = sItem.setItemStatus(img, context.getResources(), status.getId(), 0);

      if (null != customViewCallback) {
        customViewCallback.onCustomViewRequest(objects, label, image, layoutId, position, convertView, parent);
      }

      if (position == 0) {
        label.setTextColor(Color.GRAY);
        image.setAlpha(0f);
      }

      return row;
    }


    public boolean isEnabled(int position)
    {
      return position == 0 ? false : true;
    }


    public int getCount()
    {
      int count = super.getCount();
      // return count > 0 ? count - 1 : count;
      return count;
    }


    public static class SpinnerData {
      @Getter @Setter
      private String name;
      @Getter @Setter
      private int id;
    }


    public interface CustomViewCallback<T> {
      void onCustomViewRequest(List<T> data, TextView label, ImageView image, int layoutId, int position, View convertView, ViewGroup parent);
    }

  } // StatusSpinnerAdapter

}
