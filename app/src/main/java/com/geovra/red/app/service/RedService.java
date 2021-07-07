package com.geovra.red.app.service;

import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.dashboard.viewmodel.DashboardViewModel;
import com.geovra.red.shared.menu.MenuService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class RedService extends AppCompatActivity implements MenuService {
  private static final String TAG = "RedService";

  /**
   * Start new activity
   *
   * @param ctx The source activity
   * @param cls The target activity class
   */
  public <T extends AppCompatActivity> void toActivity(T ctx, Class<?> cls)
  {
    toActivity(ctx, cls, -1);
  }


  /**
   * Start new activity for result (optional)
   *
   * @param ctx The source activity
   * @param cls The target activity class
   * @param requestCode
   */
  public <T extends AppCompatActivity> void toActivity(T ctx, Class<?> cls, int requestCode)
  {
    Intent intent = new Intent(ctx, cls);
    if (requestCode > 0) {
      ctx.startActivityForResult(intent, requestCode);
    } else {
      ctx.startActivity(intent);
    }
  }

}
