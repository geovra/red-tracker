package com.geovra.red.bus;


public class Subscriber<T> {
  private Callback<Event<T>> callback;

  public Subscriber(Callback<Event<T>> callback) {
    this.callback = callback;
  }

  public void accept(Event<T> event) {
    callback.apply(event);
  }

  public static interface Callback<U> {
    public void apply(U event);
  }

}
