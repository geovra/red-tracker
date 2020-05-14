package com.geovra.red.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.R;


public class TestShow0Activity extends AppCompatActivity {

  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";


  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);

    this.setContentView(R.layout.test_show_zero);
  }

}