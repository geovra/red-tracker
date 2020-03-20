package com.geovra.red.bus;

import com.geovra.red.model.item.ItemEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Bus {
  private static Bus bus;
  private HashMap<Class, ArrayList<Subscriber>> subscribers = new HashMap<>();

  private Bus() {}

  // Event<ItemEvent.Created> event = new Event("...");
  public static <T> void emit(Class c, Event<T> event, Function<Event<T>, Void> handler) {
    for ( Subscriber sub : getInstance().subscribers.get(c) ) {
      sub.accept(event);
    }
  }


  public static Bus getInstance() {
    if (null == bus) {
      bus = new Bus();
    }
    return bus;
  }

}
