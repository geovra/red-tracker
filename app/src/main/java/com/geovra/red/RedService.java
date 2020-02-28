package com.geovra.red;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.viewmodel.DashboardViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class RedService extends AppCompatActivity {
  public static final String PAT_DD_MM_YY = "dd-MM-yyyy";

  public List<String> getDaysBetween() {
    return null;
  }


  public ArrayList<String> getIntervalDays() {
    ArrayList<String> list = new ArrayList<>();

    Calendar now = getCurrentDate();

    for (int i = 0; i < 7; i++) { // Push each day and increment by one day
      list.add( getFormat().format( now.getTime() ) );
      now.add(Calendar.DAY_OF_MONTH, 1);
    }

    System.out.println( "zzz getDaysCurrentWeek " + list.toString() );
    return list;
  }


  public Calendar getCurrentDate()
  {
    Calendar now = Calendar.getInstance();

    int delta = - now.get( GregorianCalendar.DAY_OF_WEEK ) + 2;
    now.add( Calendar.DAY_OF_MONTH, delta );

    return now;
  }


  public String getToday()
  {
    String today = getFormat().format( new Date() );
    return today;
  }

  public SimpleDateFormat getFormat() {
    SimpleDateFormat format = new SimpleDateFormat(PAT_DD_MM_YY);
    return format;
  }


  public String getDayOfMonth(String date)
  {
    String dd = date.substring(0, 2);
    return dd;
  }


  public String getDayOfWeek(String day) /* dd-MM-YYYY */
  {
    SimpleDateFormat dmy = new SimpleDateFormat(DashboardViewModel.PAT_DD_MM_YY);
    Date date = null;
    try {
      date = dmy.parse(day);
    } catch (Exception e) {}

    SimpleDateFormat ee = new SimpleDateFormat("E");

    return ee.format(date).substring(0, 1);
  }

}
