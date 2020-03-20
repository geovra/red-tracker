package com.geovra.red.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class RedPrefs {

  public static <T extends Activity> String getString(T ctx, String key, String def)
  {
    return getPrefs(ctx).getString(key, def);
  }


  public static <T extends Activity> int getInt(T ctx, String key, int def)
  {
    return getPrefs(ctx).getInt(key, def);
  }


  public static <T extends Activity> void putString(T ctx, String key, String value)
  {
    SharedPreferences.Editor editor = getEditor(ctx);
    editor.putString(key, value);
    editor.commit();
  }


  public static <T extends Activity> void putInt(T ctx, String key, int value)
  {
    SharedPreferences.Editor editor = getEditor(ctx);
    editor.putInt(key, value);
    editor.commit();
  }


  private static <T extends Activity> SharedPreferences getPrefs(T ctx)
  {
    SharedPreferences prefs = ctx.getSharedPreferences("GENERAL", Context.MODE_PRIVATE);
    return prefs;
  }


  private static <T extends Activity> SharedPreferences.Editor getEditor(T ctx)
  {
    SharedPreferences prefs = getPrefs(ctx);
    SharedPreferences.Editor editor = prefs.edit();
    return editor;
  }

}
