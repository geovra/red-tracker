package com.geovra.red.shared;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateService {
  public static final String TAG = "DateService";

  public void setDialog(Activity activity, View target, DatePickerCallback callback)
  {
    DatePickerDialog.OnDateSetListener listener = (DatePicker view, int year, int month, int day) -> {
      Log.d(TAG, String.format("%d %d %d", year, month, day));

      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      calendar.set(year, month, day);
      String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

      // binding.date.setText(date);
      // model.setDate(date);
      // binding.invalidateAll();
      if (callback != null) {
        callback.onDateSet(view, year, month, day, date);
      }
    };

    target.setOnClickListener(v -> { // Show date dialog
      Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
      DatePickerDialog dialog = new DatePickerDialog(
        activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK, listener,
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) );
      dialog.show();
    });
  }


  public interface DatePickerCallback {
    public void onDateSet(DatePicker view, int year, int month, int day, String result);
  }
}
