package com.geovra.red.shared.menu;

import androidx.appcompat.app.AppCompatActivity;

public interface MenuService {
  public <T extends AppCompatActivity> void toActivity(T ctx, Class<?> cls);
  public <T extends AppCompatActivity> void toActivity(T ctx, Class<?> cls, int requestCode);
}
