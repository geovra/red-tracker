package com.geovra.red.shared;

import android.content.Context;

public class Toast {
  // ...

  public static int LENGTH_SHORT = 0;
  public static int LENGTH_LONG = 1;

  public static void show(Context ctx, String text, int duration)
  {
    android.widget.Toast.makeText(ctx, text, duration).show();
  }

  public static void show(Context ctx, int textId, int duration)
  {
    android.widget.Toast.makeText(ctx, textId, duration).show();
  }
}
