package com.geovra.red;

import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

//import com.geovra.red.databinding.BarIndexBindingImpl;
//import com.geovra.red.databinding.FooIndexBindingImpl;
import com.geovra.red.preview.tdi.FragmentBasic;
import com.geovra.red.preview.tdi.TabAdapter;
import com.geovra.tdi.TdiActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;


public class RedGateway {
  private static String APP_TDI = "com.geovra.tdi.TdiActivity";
  private static String APP_TRACKER = "com.geovra.tdi.TrackerActivity";

  private TabAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;

  protected void onCreate(AppCompatActivity act, Bundle state)
  {
    //ActivityMainBinding binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
    //FooIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.foo_index);
    //BarIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.test_zero_index);
    //TdiActivityMainBinding bindingTdi = DataBindingUtil.setContentView(act, com.geovra.tdi.R.layout.tdi_activity_main);

    //binding.msg.setText("tdi: appbar");

    act.setContentView(R.layout.test_zero_index);

    final AppCompatActivity _act = act;
    final CollapsingToolbarLayout toolbar = act.findViewById(R.id.ctlCollaps);
    NestedScrollView scroll = (NestedScrollView) act.findViewById(R.id.nsvContent);

    scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int diffY = oldScrollY - scrollY;
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();

        if (diffY > 17) {
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

  public void toCoordinatorLayout(AppCompatActivity act, Bundle state)
  {
    //binding.msg.setText("tdi: appbar");

    act.setContentView(R.layout.test_zero_index);

    Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar);
    act.setSupportActionBar(toolbar);

    viewPager = (ViewPager) act.findViewById(R.id.viewPager);
    tabLayout = (TabLayout) act.findViewById(R.id.tabLayout);

    adapter = new TabAdapter( act.getSupportFragmentManager() );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_one), "L" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_two), "M" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_one), "Today" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_two), "J" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_two), "V" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_two), "S" );
    adapter.addFragment( new FragmentBasic(R.layout.pre_tdi_fragment_two), "D" );

    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);


    /* getCurrentApp()
      .getMainActivity()
      .onCreate(state); */

    /* act.setContentView(com.geovra.tdi.R.layout.tdi_activity_main); */

  }

  private void toBaseLayout(AppCompatActivity act, Bundle state) {

  }

}
