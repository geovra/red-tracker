package com.geovra.red.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.geovra.red.R;
import com.geovra.tdi.TdiActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class TestShow0Activity extends AppCompatActivity {

  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";


  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);

    this.setContentView(R.layout.test_show_zero);
  }

}