package com.geovra.red.shared;

import android.content.Context;

import com.geovra.red.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

  public static String getTimeString(Context ctx, Date fromdate) {

    long then;
    then = fromdate.getTime();
    Date date = new Date(then);

    StringBuffer dateStr = new StringBuffer();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    Calendar now = Calendar.getInstance();

    int weeks = weeksBetween(calendar.getTime(), now.getTime());
    int days = daysBetween(calendar.getTime(), now.getTime());
    int minutes = hoursBetween(calendar.getTime(), now.getTime());
    int hours = minutes / 60;

    if (days == 0) {
      int second = minuteBetween(calendar.getTime(), now.getTime());
      if (minutes > 60) {
        if (hours >= 1 && hours <= 24) {
          String value = ctx.getResources().getQuantityString(R.plurals.hours_ago, hours, hours);
          dateStr.append(value);
        }
      }

      else {
        if (second <= 10) {
          dateStr.append("Now");
        } else if (second > 10 && second <= 30) {
          dateStr.append("few seconds ago");
        } else if (second > 30 && second <= 60) {
          String value = ctx.getResources().getQuantityString(R.plurals.seconds_ago, second, second);
          dateStr.append(value);
        } else if (second >= 60 && minutes <= 60) {
          String value = ctx.getResources().getQuantityString(R.plurals.minutes_ago, minutes, minutes);
          dateStr.append(value);
        }
      }
    }

    else if (hours > 24 && days <= 7) {
      String value = ctx.getResources().getQuantityString(R.plurals.days_ago, days, days);
      dateStr.append(value);
    }

    else if (days <= 30) {
      String value = ctx.getResources().getQuantityString(R.plurals.weeks_ago, days / 7, days / 7);
      dateStr.append(value);
    }

    else if (weeks <= 3) {
      String value = ctx.getResources().getQuantityString(R.plurals.weeks_ago, weeks, weeks);
      dateStr.append(value);
    }

    else if (weeks <= 4) {
      String value = ctx.getResources().getQuantityString(R.plurals.months_ago, 1,1);
      dateStr.append(value);
    }

    else if (days <= 365) {
      String value = ctx.getResources().getQuantityString(R.plurals.months_ago, days / 30, days / 30);
      dateStr.append(value);
    }

    else {
      String value = ctx.getResources().getQuantityString(R.plurals.months_ago, days / 365, days / 365);
      dateStr.append(value);
    }

    return dateStr.toString();
  }


  public static int minuteBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.SECOND_IN_MILLIS);
  }

  public static int hoursBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.MINUTE_IN_MILLIS);
  }


  public static int daysBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.DAY_IN_MILLIS);
  }


  public static int weeksBetween(Date d1, Date d2) {
    return (int) ((d2.getTime() - d1.getTime()) / android.text.format.DateUtils.WEEK_IN_MILLIS);
  }


  public static String format(String input, String format)
  {
    try {
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(input);
      boolean isSameYear = new SimpleDateFormat("yyyy").format(new Date()).equals(new SimpleDateFormat("yyyy").format(date));

      if ( isSameYear && (null == format)) {
        format = "EEE, dd MMMM";
      }

      format = (null != format) ? format : "yyyy-MM-dd";

      return new SimpleDateFormat(format).format(date);
    } catch (Exception e) {
      e.getMessage();
    }
    return input;
  }


  public static String format(String input, String format, Context ctx)
  {
    String result = format(input, format);
    String today = format( new SimpleDateFormat("yyyy-MM-dd").format(new Date()), format );
    try {
      if (result.equals(today)) {
        result = ctx.getResources().getString(R.string.today);
      }
    } catch (Exception e) {
      e.getMessage();
    }
    return result;
  }


  public static String getDateDifferenceForDisplay(Date inputdate) {
    Calendar now = Calendar.getInstance();
    Calendar then = Calendar.getInstance();

    now.setTime(new Date());
    then.setTime(inputdate);

    // Get the represented date in milliseconds
    long nowMs = now.getTimeInMillis();
    long thenMs = then.getTimeInMillis();

    // Calculate difference in milliseconds
    long diff = nowMs - thenMs;

    // Calculate difference in seconds
    long diffMinutes = diff / (60 * 1000);
    long diffHours = diff / (60 * 60 * 1000);
    long diffDays = diff / (24 * 60 * 60 * 1000);

    if (diffMinutes < 60) {
      return diffMinutes + " m";

    } else if (diffHours < 24) {
      return diffHours + " h";

    } else if (diffDays < 7) {
      return diffDays + " d";

    } else {
      SimpleDateFormat todate = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

      return todate.format(inputdate);
    }
  }

}
