package com.geovra.red.app.http;

import java.util.HashMap;

public class RequestBag {
  private RequestBag bag;
  private HashMap<String, Object> data;
  private HashMap<String, String> typeMeta;

  public RequestBag add(String key, String value)
  {
    if (null == bag) {
      bag = new RequestBag();
    }
    bag.getData().put(key, value);
    return bag;
  }

  public RequestBag add(String key, int value)
  {
    if (null == bag) {
      bag = new RequestBag();
    }
    bag.getData().put(key, value);
    return bag;
  }

  public String get(String key)
  {
    return (String) data.get(key);
  }

  public int getInt(String key)
  {
    Object value = data.get(key);
    return (int) value;
  }

  public String getString(String key)
  {
    Object value = data.get(key);
    return (String) value;
  }

  public void remove(String key)
  {
    data.remove(key);
  }

  public HashMap<String, Object> getData()
  {
    return data;
  }

}
