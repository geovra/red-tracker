package com.geovra.red.adapter.item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geovra.red.R;
import com.geovra.red.model.item.Status;

import java.util.List;

public class ItemAdapterBase {


  // item::spinner
  public static class StatusSpinnerAdapter extends ArrayAdapter<Status> {
    private List<Status> objects;
    private Context context;

    public StatusSpinnerAdapter(Context context, int resourceId, List<Status> objects) {
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

      TextView label = (TextView) row.findViewById(R.id.status);
      Status status = objects.get(position);
      label.setText(status.getName());
      label.setTag(status.getId());

      if (position == 0) {
        label.setTextColor(Color.GRAY);
      }

      // ImageView color = (ImageView) row.findViewById(R.id.color);
      // ...

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

  }

}
