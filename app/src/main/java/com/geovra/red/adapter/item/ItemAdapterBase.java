package com.geovra.red.adapter.item;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.geovra.red.R;

import java.util.List;

public class ItemAdapterBase {

  // item::spinner
  public static class StatusSpinnerAdapter extends ArrayAdapter<String> {
    private List<String> objects;
    private Context context;

    public StatusSpinnerAdapter(Context context, int resourceId, List<String> objects) {
      super(context, resourceId, objects);
      this.objects = objects;
      this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
      return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );

      View row = inflater.inflate(R.layout.item_spinner_entry, parent, false);
      TextView label = (TextView) row.findViewById(R.id.status);
      label.setText(objects.get(position));
      if (position == 0) {
        label.setTextColor(Color.GRAY);
      }
      // if (position == 0) {//Special style for dropdown header
      //   label.setTextColor(context.getResources().getColor(R.color.text_hint_color));
      // }

      return row;
    }

    public boolean isEnabled(int position) {
      return position == 0 ? false : true;
    }

    public int getCount() {
      int count = super.getCount();
      // return count > 0 ? count - 1 : count;
      return count;
    }

  }

}
