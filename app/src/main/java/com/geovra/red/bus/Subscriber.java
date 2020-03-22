package com.geovra.red.bus;


public class Subscriber<T> {
  private Callback<T> callback;

  public Subscriber(Callback<T> callback) {
    this.callback = callback;
  }

  public void accept(T event) {
    callback.apply(event);
  }

  public static interface Callback<U> {
    public void apply(U event);
  }

}
