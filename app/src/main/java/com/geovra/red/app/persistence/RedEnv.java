package com.geovra.red.app.persistence;

import android.content.res.AssetManager;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class RedEnv {

  /**
   * Read environment properties
   *
   * @return Properties
   */
  public static Properties init(AppCompatActivity target) {
    Resources resources = target.getResources();
    AssetManager assetManager = resources.getAssets();

    try {
      Properties properties = new Properties();
      properties.load(assetManager.open("app.properties"));

      // Write properties to SharedPreferences for global access
      @SuppressWarnings("unchecked")
      Enumeration<String> it = (Enumeration<String>) properties.propertyNames();
      RedPrefs prefs = new RedPrefs(target.getApplicationContext());
      while (it.hasMoreElements()) {
        String key = it.nextElement();
        prefs.putString(key, properties.getProperty(key));
      }

      return properties;
    } catch (Exception e) {
      System.err.println("Failed to open properties file");
      e.printStackTrace();
    }

    return null;
  }
}
