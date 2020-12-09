package com.geovra.red.shared.date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.geovra.red.dashboard.viewmodel.DashboardViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateService {
  public static final String TAG = "DateService";
  public static final String PAT_DD_MM_YY = "dd-MM-yyyy";
  public static final String PAT_YY_MM_DD = "yyyy-MM-dd";

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


  /**
   * Return current date in yyyy-MM-dd format
   */
  public String getToday() {
    return getToday("yyyy-MM-dd");
  }


  public String getToday(String format) {
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    return date;
  }


  /**
   * Read days for current week
   */
  public ArrayList<String> getIntervalDays()
  {
    return getIntervalDays("w");
  }


  /**
   * Read days for given interval
   */
  public ArrayList<String> getIntervalDays(String interval)
  {
    ArrayList<String> days = new ArrayList<>();

    if ("w".equals(interval)) {
      days = getDaysWeek();
    }

    if ("m".equals(interval)) {
      days = getDaysMonth();
    }

    if (interval.contains("_")) {
      days = getDaysInterval(interval);
    }

    return days;
  }


  /**
   * Read days for current week
   */
  public ArrayList<String> getDaysWeek()
  {
    ArrayList<String> list = new ArrayList<>();
    Calendar now = getCurrentDate();

    for (int i = 0; i < 7; i++) { // Push each day and increment by one day
      list.add( getFormat().format( now.getTime() ) );
      now.add(Calendar.DATE, 1);
    }

    Log.d(TAG, "getIntervalDays " + list.toString());
    return list;
  }


  /**
   * Read days for current month
   */
  public ArrayList<String> getDaysMonth()
  {
    ArrayList<String> days = new ArrayList<>();
    Log.d(TAG, "getDaysMonth " + days.toString());
    return days;
  }


  /**
   * Read days for given interval yyyy-MM-dd_yyyy-MM-dd
   */
  public ArrayList<String> getDaysInterval(String interval)
  {
    ArrayList<String> days = new ArrayList<>();

    try {
      String dateLeft = interval.split("_")[0];
      String dateRight = interval.split("_")[1];

      Date date = new SimpleDateFormat(PAT_YY_MM_DD).parse(dateLeft);
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);

      int maxAttempts = 999, number = 0;
      do {
        days.add(new SimpleDateFormat(PAT_YY_MM_DD).format(cal.getTime()));
        cal.add(Calendar.DATE, 1);
      } while (! days.get(days.size() - 1).equals(dateRight) && (number++ < maxAttempts));

    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
    }

    Log.d(TAG, "getDaysInterval " + days.toString());
    return days;
  }


  public Calendar getCurrentDate()
  {
    Calendar now = Calendar.getInstance();

    now.setFirstDayOfWeek(Calendar.MONDAY);
    now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    // int delta = - now.get( GregorianCalendar.DAY_OF_WEEK ) + 2;
    // now.add( Calendar.DAY_OF_MONTH, delta );

    return now;
  }


  public SimpleDateFormat getFormat() {
    SimpleDateFormat format = new SimpleDateFormat(PAT_YY_MM_DD);
    return format;
  }


  public String getDayOfMonth(String date)
  {
    String dd = date.substring(date.length() - 2, date.length());
    return dd;
  }


  public String getDayOfWeek(String day) /* dd-MM-YYYY */
  {
    SimpleDateFormat dmy = new SimpleDateFormat(DashboardViewModel.PAT_YY_MM_DD);
    Date date = null;
    try {
      date = dmy.parse(day);
    } catch (Exception e) {}

    SimpleDateFormat ee = new SimpleDateFormat("E");

    return ee.format(date).substring(0, 1);
  }


  public List<String> getDaysBetween()
  {
    return null;
  }
}
