package com.geovra.red.app.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class RedPrefs {
  private SharedPreferences prefs;
  private SharedPreferences.Editor editor;

  public String getString(Context ctx, String key)
  {
    return getString(ctx, key, null);
  }


  public String getString(Context ctx, String key, String def)
  {
    return getPrefs(ctx).getString(key, def);
  }


  public int getInt(Context ctx, String key, int def)
  {
    return getPrefs(ctx).getInt(key, def);
  }


  public RedPrefs putString(Context ctx, String key, String value)
  {
    SharedPreferences.Editor editor = getEditor(ctx);
    editor.putString(key, value);
    editor.commit();
    return this;
  }


  public RedPrefs putInt(Context ctx, String key, int value)
  {
    SharedPreferences.Editor editor = getEditor(ctx);
    editor.putInt(key, value);
    editor.commit();
    return this;
  }


  private SharedPreferences getPrefs(Context ctx)
  {
    if (null == prefs) {
      prefs = ctx.getSharedPreferences("GENERAL", Context.MODE_PRIVATE);
    }
    return prefs;
  }


  private SharedPreferences.Editor getEditor(Context ctx)
  {
    if (null == editor) {
      SharedPreferences prefs = getPrefs(ctx);
      editor = prefs.edit();
    }
    return editor;
  }
}
