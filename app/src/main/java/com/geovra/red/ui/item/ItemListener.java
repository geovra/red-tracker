package com.geovra.red.ui.item;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.geovra.red.RedActivity;
import com.geovra.red.model.item.Item;
import com.google.gson.Gson;

public class ItemListener {


  public static class OnUpdate {
    public static View.OnClickListener getInstance(RedActivity act, Item item) {
      return (View v) -> {
        Intent intent = new Intent(act, ItemCreateUpdateActivity.class);
        intent.putExtra("_type", "UPDATE");
        intent.putExtra("item", new Gson().toJson(item));
        act.startActivity(intent);
      };
    }
  }


  public static class FocusChange {
    public static <T extends RedActivity> View.OnFocusChangeListener getListener(int id, T ctx) {
      return (View v, boolean hasFocus) -> {
        if (id == v.getId() && ! hasFocus) {
          ctx.keyboardHide(v.getWindowToken(), ctx);
        }
      };
    }
  }


}
