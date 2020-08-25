package com.geovra.red.shared.tab.ui;

import android.util.Pair;

import java.util.HashMap;

public class TabPayload {
  public HashMap<Pair<String, Class<?>>, Object> values;

  public <T> TabPayload put(String id, Class<T> class_, T value_)
  {
    class_.cast(
      values.put(
        new Pair<>(id, class_), value_
      )
    );

    return this;
  }


  @SuppressWarnings("unchecked")
  public <T> T get(String id, Class<T> class_) {
    Pair<String, Class<T>> key = new Pair<>(id, class_);

    Object found = values.containsKey(key)
      ? values.get(key)
      : null;

    return found != null ? (T) found : null;
  }
}
