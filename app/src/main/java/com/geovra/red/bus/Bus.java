package com.geovra.red.bus;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.model.item.ItemEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class Bus {
  public static final String TAG = "Bus";
  private static Bus bus;
  private HashMap<Class, ArrayList<Subscriber>> subscribers = new HashMap<>();
  private HashMap<Class, ArrayList<Object>> events = new HashMap<>();
  public static boolean EVENTS_KEEP = true;
  public static boolean EVENTS_REMOVE = false;

  private Bus()
  {}


  // Event<ItemEvent.Created> event = new Event("...");
  public static <T> void emit(Class c, Event<T> event)
  {
    try {
      for ( Subscriber sub : getInstance().subscribers.get(c) ) {
        sub.accept(event);
      }
    }
    catch(Exception e) {
      Log.e(TAG, e.toString());
    }
  }


  public static <T> void listen(Class c, Subscriber.Callback<T> callback)
  {
    Subscriber sub = new Subscriber<T>(callback);

    ArrayList<Subscriber> list = getInstance().subscribers.get(c);
    if (null == list) {
      list = new ArrayList<>();
    }
    list.add(sub);

      getInstance().subscribers.put(c, list);
  }


  public static void push(Class c, Object event)
  {
    ArrayList<Object> list = getInstance().events.get(c);
    list = (null != list) ? list : new ArrayList<>();
    list.add(event);
    getInstance().events.put(c, list);
  }


  public static void replace(Class c, Object event)
  {
    ArrayList<Object> list = new ArrayList<>();
    list.add(event);
    getInstance().events.put(c, list);
  }


  public static <T> void consume(AppCompatActivity activity, boolean isKeepEvents)
  {
    for ( Class c : getInstance().events.keySet() ) {
      for ( Object obj : getInstance().events.get(c) ) {
        emit(c, (Event<T>) obj);
      }
      if (! isKeepEvents) { // When specified to remove the events
        getInstance().events.put(c, new ArrayList<>());
      }
    }
  }


  public static Bus getInstance() {
    if (null == bus) {
      bus = new Bus();
    }
    return bus;
  }

}
