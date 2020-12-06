package com.geovra.red.shared.bus;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;

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


  /**
   * Do not use this unless you don't care about subscriber duplication.
   */
  public static <T> String listen(Class c, Subscriber.Callback<T> callback)
  {
    Subscriber sub = new Subscriber<T>(callback);

    ArrayList<Subscriber> list = getInstance().subscribers.get(c);
    if (null == list) {
      list = new ArrayList<>();
    }
    list.add(sub);

    getInstance().subscribers.put(c, list);

    return sub.getId();
  }


  public static <T> String listen(Disposable disposable, Class c, Subscriber.Callback<T> callback)
  {
    String id = listen(c, callback);
    disposable.push(id);
    return id;
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


  public static void dispose(Disposable d)
  {
    HashMap<Class, ArrayList<Subscriber>> subscribersList = new HashMap<>();
    Set<Class> _c = getInstance().subscribers.keySet();
    Iterator<Class> it = _c.iterator();

    while (it.hasNext()) { // every Class
      Class classCurrent = it.next();
      subscribersList.put(classCurrent, new ArrayList<>()); // fresh list of subscribers
      ArrayList<Subscriber> _subscribers = getInstance().subscribers.get(classCurrent);

      if (null == _subscribers) { continue; }
      for (Subscriber _s : _subscribers ) { // every Subscriber
        boolean isDisposable = d.getContainer().contains(_s.getId());
        if (isDisposable) {
          continue;
        }

        try {
          subscribersList.get(classCurrent).add(_s);
        } catch (Exception e) {
          Log.d(TAG, e.toString());
        }
      }
    }

    getInstance().subscribers = subscribersList;
  }


  public static Bus getInstance() {
    if (null == bus) {
      bus = new Bus();
    }
    return bus;
  }


  public static class Disposable {
    @Getter private List<String> container = new ArrayList<>();

    public Disposable() {}

    public void push(String id) {
      container.add(id);
    }

  }

}
