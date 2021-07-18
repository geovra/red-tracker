package com.geovra.red.dashboard;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.app.http.HttpMock;
import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.category.persistence.Category;
import com.geovra.red.comment.persistence.CommentRepo;
import com.geovra.red.filter.persistence.FilterOutput;
import com.geovra.red.item.http.ItemResponse.ItemRemove;
import com.geovra.red.item.http.ItemResponse.ItemStore;
import com.geovra.red.item.http.ItemResponse.ItemUpdate;
import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemEvent;
import com.geovra.red.item.persistence.ItemRepo;
import com.geovra.red.shared.bus.Bus;
import com.geovra.red.shared.bus.Event;
import com.geovra.red.shared.date.DateService;
import com.geovra.red.status.persistence.Status;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Observable;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class DashboardViewModel extends RedViewModel {
  private static final String TAG = "DashboardViewModel";
  private static DashboardViewModel instance;
  @Getter @Setter public ItemRepo itemRepo;
  @Getter @Setter public CommentRepo commentRepo;
  @Getter @Setter public DateService dateService;
  public Item itemCurrent;
  public HttpMock http;

  public static final String PAT_DD_MM_YY = "dd-MM-yyyy";
  public static final String PAT_YY_MM_DD = "yyyy-MM-dd";
  public static final String INTERVAL_WEEK = "w";
  @Getter @Setter private String intervalName = INTERVAL_WEEK;

  @Getter @Setter private ArrayList<String> items;
  @Getter @Setter private MutableLiveData<List<Item>> dItems = new MutableLiveData<>();
  @Getter @Setter private MutableLiveData<List<Item>> dItemsResponse = new MutableLiveData<>();

  @Getter @Setter private MutableLiveData<Date> dDateCurrent = new MutableLiveData<>();
  @Getter @Setter private MutableLiveData<ArrayList<String>> intervalDays = new MutableLiveData<>();
  @Getter @Setter private MutableLiveData<String> commentNewText = new MutableLiveData<>();

  public DashboardViewModel(@NonNull Application application)
  {
    super(application);

    intervalDays.setValue(readIntervalDates("w"));
    itemRepo = new ItemRepo(new WeakReference<Context>(application.getApplicationContext()));
    dateService = new DateService();
    commentRepo = new CommentRepo(application.getApplicationContext());
  }


  public String onOptionsItemSelected(MenuItem item)
  {
    return "Clicked on " + item.getTitle();
  }


  public void readItems(Context ctx, String interval)
  {
    readItems(ctx, interval, null, null);
  }


  public void readItems(Context ctx, String interval, List<Status> statusList, List<Category> categoryList)
  {
    intervalDays.setValue(dateService.getIntervalDays(interval));

    itemRepo.findAll(ctx, interval, statusList, categoryList)
      .subscribe(
        res -> onItemsResponse(res.body().getData()),
        error -> {
          Log.d(TAG, error.toString());
          error.printStackTrace();
        },
        () -> Log.d(TAG, "200 readItems")
      );
  }


  public void readItemsByInterval(Context ctx, FilterOutput filterOutput)
  {
    readItems(ctx, filterOutput.getDateFrom() + "_" + filterOutput.getDateTo());
  }


  public void readItemsByFilters(Context ctx, FilterOutput filterOutput)
  {
    readItems(ctx, filterOutput.getDateFrom() + "_" + filterOutput.getDateTo(), filterOutput.getStatus(), filterOutput.getCategories());
  }


  public List<Item> readViewableItems(List<Item> items, Date date)
  {
    List<Item> output = new ArrayList<>();
    if (null == items)
      return output;

    String day = "";
    try {
      day = new SimpleDateFormat("yyyy-MM-dd").format(date);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < items.size(); i++) {
      Item item = items.get(i);
      try {
        boolean isEqual = item.getDate().substring(0, 10).equals(day); // yyyy-MM-dd
        boolean isContinuous = (item.getIsContinuous() + "").equals("1");

        if (isEqual || isContinuous) { // stream() much?
          output.add(item);
        }
      } catch(Exception e) {}
    }

    return output;
  }


  public void itemsViewableUpdate(Date date)
  {
    List<Item> items = readViewableItems(getDItemsResponse().getValue(), date);
    getDItems().setValue(items);
  }


  public void onItemsResponse(List<Item> data)
  {
    dItemsResponse.setValue(data);
    Log.d(TAG, new Gson().toJson(data));
  }


  public void onItemsError(Throwable error)
  {
    Log.d(TAG, error.toString());
    error.printStackTrace();
  }


  public Item readItem(int index)
  {
    return dItems.getValue().get(index);
  }


  public ArrayList<String> readIntervalDates(String interval)
  {
    ArrayList<String> list = new ArrayList<>();

    Calendar now = readCurrentDate();
    for (int i = 0; i < 7; i++) { // Push each day and increment by one day
      list.add( getFormat().format( now.getTime() ) );
      now.add(Calendar.DAY_OF_MONTH, 1);
    }

    Log.d(TAG, list.toString() );
    return list;
  }


  public Calendar readCurrentDate()
  {
    Calendar now = Calendar.getInstance();
    now.setFirstDayOfWeek(Calendar.MONDAY);

    int delta = - ( now.get( GregorianCalendar.DAY_OF_WEEK ) - 1 );
    now.add( Calendar.DAY_OF_MONTH, delta );

    return now;
  }


  public <T> T readDateByPosition(int position, String format, String r)
  {
    Calendar now = Calendar.getInstance();
    now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // Assume position in week
    now.add(Calendar.DAY_OF_WEEK, position);
    Date date = now.getTime();

    format = ! format.isEmpty() ? format : "yyyy-MM-dd";
    String formatted = dateFormat(date, format);
    T result = null;

    switch (r) {
      case "date":
        result = (T) date;
        break;
      case "string":
        result = (T) formatted;
        break;
    }

    return result;
  }


  public String dateFormat(Date date, String format)
  {
    SimpleDateFormat f = new SimpleDateFormat(format);
    return f.format(date);
  }


  public SimpleDateFormat getFormat()
  {
    SimpleDateFormat f = new SimpleDateFormat(PAT_YY_MM_DD);
    return f;
  }


  public ArrayList<Date> getDaysOfWeek()
  {
    ArrayList<Date> days = new ArrayList<>();
    // ...
    return days;
  }


  public Date getTime()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    Date time = calendar.getTime();
    return time;
  }


  public int getDayOfWeek()
  {
    int res = -1;
    String str = getTime().toString().substring(0, 3);
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

    Log.d(TAG, "getDayOfWeek: " + res);
    return res;
  }


  public void setItemsData(MutableLiveData<List<Item>> dItems)
  {
    this.dItems = dItems;
  }


  public Observable<Response<ItemStore>> itemStore(Context ctx, Item item)
  {
    return itemRepo.store(ctx, item)
      .map(response -> {
        Bus.replace(ItemEvent.Created.class, new Event<ItemEvent.Created>(
          new ItemEvent.Created(item))
        );

        return response;
      })
      .doOnError(err -> {
          Log.d(TAG, String.format("%s %s", "ItemStore", err.toString()));
          err.printStackTrace();
      });
  }


  public Observable<Response<ItemUpdate>> itemUpdate(Context ctx, Item item)
  {
    return itemRepo.update(ctx, item)
        .map(response -> {
          Bus.replace(ItemEvent.Updated.class, new Event<ItemEvent.Updated>(
            new ItemEvent.Updated(item))
          );

          return response;
        })
        .doOnError(err -> {
          Log.d(TAG, String.format("%s %s", "ItemUpdate", err.toString()));
          err.printStackTrace();
        });
  }


  public Observable<Response<ItemRemove>> itemRemove(Context ctx, Item item)
  {
    return itemRepo.remove(ctx, item);
  }


  public static DashboardViewModel getInstance(Application application)
  {
    if (null == instance) {
      instance = new DashboardViewModel(application);
    }
    return instance;
  }
}
