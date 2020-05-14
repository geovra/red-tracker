package com.geovra.red.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.geovra.red.R;
import com.geovra.tdi.TdiActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class Test0Activity extends AppCompatActivity {

  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";


  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);

    //ActivityMainBinding binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
    //FooIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.foo_index);
    //BarIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.test_zero_index);
    //TdiActivityMainBinding bindingTdi = DataBindingUtil.setContentView(act, com.geovra.tdi.R.layout.tdi_activity_main);

    //binding.msg.setText("tdi: appbar");

    this.setContentView(R.layout.test_zero_index);

    final AppCompatActivity _act = this;
    final CollapsingToolbarLayout toolbar = this.findViewById(R.id.ctlCollaps);
    NestedScrollView scroll = (NestedScrollView) this.findViewById(R.id.nsvContent);

    scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int diffY = oldScrollY - scrollY;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        if (diffY > 15) {
          System.out.println("onScrollChange() 200 bY-aY = " + diffY);
          params.setScrollFlags(
                  AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                          AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS |
                          AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED );
          toolbar.setLayoutParams(params);
        }
        else {
          params.setScrollFlags(
                  AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                          AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED );
          toolbar.setLayoutParams(params);
        }
      }
    });


    /* getCurrentApp()
      .getMainActivity()
      .onCreate(state); */

    AppCompatActivity tdi = new TdiActivity();
    System.out.println("zzz" + tdi.toString());
  }

}
