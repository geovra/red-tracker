package com.geovra.red.shared;

import android.content.Intent;

import com.geovra.red.app.provider.RedIntentProvider;
import com.geovra.red.app.ui.RedActivity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Payload {
  @Getter
  Map<String, String> map = new HashMap<>();

  public void put(String key, String value)
  {
    map.put(key, value);
  }


  public void fill(RedIntentProvider.Action action) {
    Iterator it = map.keySet().iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      action.data(key, map.get(key));
    }
  }
}
