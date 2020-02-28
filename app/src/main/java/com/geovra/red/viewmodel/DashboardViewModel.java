package com.geovra.red.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MenuItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.RedViewModel;
import com.geovra.red.http.item.ItemResponse;
import com.geovra.red.http.item.ItemService;
import com.geovra.red.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observable;

@SuppressLint("CheckResult")
public class DashboardViewModel extends RedViewModel {
  private static final String TAG = "DashboardViewModel";
  public static final String PAT_DD_MM_YY = "dd-MM-yyyy";
  private ArrayList<String> intervalDays;
  private ArrayList<String> items;
  public final static String INTERVAL_WEEK = "INTERVAL_WEEK";
  private MutableLiveData<List<Item>> dItems = new MutableLiveData<>();
  private ItemService sItem;

  public DashboardViewModel()
  {
    intervalDays = readIntervalDates(null);
    sItem = (new ItemService());
  }


  public String onOptionsItemSelected(MenuItem item)
  {
    return "Clicked on " + item.getTitle();
  }


  public void readItems()
  {
    sItem.findAll()
      .subscribe(
        res -> {
          Log.d(TAG, res.toString());
          dItems.setValue(res.getData()); // 200
        },
        error -> {
          Log.d(TAG, error.toString());
          error.printStackTrace();
        },
        () -> Log.d(TAG, "200 readItems")
      );

    // ArrayList<String> list = new ArrayList<String>();
    // list.add(". Put 5 new holes in your belt");
    // list.add(". Reinstall OS (backup, install, restore)");
    // list.add(". Withdraw 3000ron for Ana's wedding");
    // list.add(". Click listeners on items");
    // return list;
  }


  public ArrayList<String> readIntervalDates(String interval)
  {
    ArrayList<String> list = new ArrayList<>();

    Calendar now = readCurrentDate();
    for (int i = 0; i < 7; i++) { // Push each day and increment by one day
      list.add( getFormat().format( now.getTime() ) );
      now.add(Calendar.DAY_OF_MONTH, 1);
    }

    System.out.println( "zzz " + list.toString() );
    return list;
  }


  public Calendar readCurrentDate()
  {
    Calendar now = Calendar.getInstance();
    int delta = - now.get( GregorianCalendar.DAY_OF_WEEK ) + 2;
    now.add( Calendar.DAY_OF_MONTH, delta );
    return now;
  }


  public SimpleDateFormat getFormat()
  {
    SimpleDateFormat f = new SimpleDateFormat(PAT_DD_MM_YY);
    return f;
  }


  public ArrayList<Date> getDaysOfWeek()
  {
    ArrayList<Date> days = new ArrayList<>();
    // ...
    return days;
  }


  public int getDayOfWeek()
  {
    int res = -1;
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    String str = calendar.getTime().toString().substring(0, 3);
    switch (str) {
      case "Mon":
        res = 1;
        break;
      case "Tue":
        res = 2;
        break;
      case "Wed":
        res = 3;
        break;
      case "Thu":
        res = 4;
        break;
      case "Fri":
        res = 5;
        break;
      case "Sat":
        res = 6;
        break;
      case "Sun":
        res = 7;
        break;
    }

    System.out.println("zzz getDayOfWeek: " + res);
    return res;
  }


  public ArrayList<String> getIntervalDays()
  {
    return intervalDays;
  }


  public ArrayList<String> getItems()
  {
    return items;
  }


  public LiveData<List<Item>> getItemsData()
  {
    return dItems;
  }


  public void setItemsData(MutableLiveData<List<Item>> dItems)
  {
    this.dItems = dItems;
  }


  public List<String> getItemStatusOptions()
  {
    List<String> options = new ArrayList<>();
    options.add("Choose status");
    options.add("OPT1");
    options.add("OPT2");
    options.add("OPT3");
    return options;
  }


  public Observable<ItemResponse.ItemStore> itemStore(Item item)
  {
    return sItem.store(item);
  }
}
