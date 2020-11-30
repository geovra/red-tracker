package com.geovra.red.shared.bus;

// new Event<ItemEvent.CREATED>(...);
public class Event<T> {
  public String name;
  public T payload;
  public Class type;

  public Event(T payload) {
    this.payload = payload;
  }


  public Event(String name) {
    this.name = name;
  }


  public Event(String name, Class c, T payload) {
    this(name);
    this.type = c;
    this.payload = payload;
  }


  public void setPayload(Class c, T payload) {
    this.payload = payload;
    this.type = c;
  }


  public T getPayload() {
    // T output =  payload;
    return payload;
  }

}
