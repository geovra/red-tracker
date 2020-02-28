package com.geovra.red.ui.item;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.RedActivity;

public class ItemListener {

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
