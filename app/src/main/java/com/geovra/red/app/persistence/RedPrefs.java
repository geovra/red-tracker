package com.geovra.red.app.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class RedPrefs {
  private Context ctx;

  public RedPrefs(Context ctx) {
    this.ctx = ctx;
  }


  public String getString(String key)
  {
    return getString(key, null);
  }


  public String getString(String key, String def)
  {
    return getPrefs().getString(key, def);
  }


  public int getInt(String key, int def)
  {
    return getPrefs().getInt(key, def);
  }


  public void putString(String key, String value)
  {
    SharedPreferences.Editor editor = getEditor();
    editor.putString(key, value);
    editor.commit();
  }


  public void putInt(String key, int value)
  {
    SharedPreferences.Editor editor = getEditor();
    editor.putInt(key, value);
    editor.commit();
  }


  private SharedPreferences getPrefs()
  {
    SharedPreferences prefs = ctx.getSharedPreferences("GENERAL", Context.MODE_PRIVATE);
    return prefs;
  }


  private SharedPreferences.Editor getEditor()
  {
    SharedPreferences prefs = getPrefs();
    SharedPreferences.Editor editor = prefs.edit();
    return editor;
  }
}
