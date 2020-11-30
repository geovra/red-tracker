package com.geovra.red.shared.bus;


import java.nio.charset.Charset;
import java.util.Random;

import lombok.Getter;

public class Subscriber<T> {
  private Callback<T> callback;
  @Getter private String id;

  public Subscriber(Callback<T> callback) {
    byte[] list = new byte[16];
    new Random().nextBytes(list);
    this.id = new String(list, Charset.forName("UTF-8"));
    this.callback = callback;
  }

  public void accept(T event) {
    callback.apply(event);
  }

  public static interface Callback<U> {
    public void apply(U event);
  }

}
