package com.geovra.red.bus;

import android.util.Log;

import com.geovra.red.model.item.ItemEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class Bus {
  public static final String TAG = "Bus";
  private static Bus bus;
  private HashMap<Class, ArrayList<Subscriber>> subscribers = new HashMap<>();

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


  public static <T> void listen(Class c, Subscriber.Callback<Event<T>> callback)
  {
    Subscriber sub = new Subscriber<T>(callback);

    ArrayList<Subscriber> list = getInstance().subscribers.get(c);
    if (null == list) {
      list = new ArrayList<>();
    }
    list.add(sub);

    getInstance().subscribers.put(c, list);
  }


  public static Bus getInstance() {
    if (null == bus) {
      bus = new Bus();
    }
    return bus;
  }

}
