package com.geovra.red;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.geovra.tdi.TdiActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


public class Test1Activity extends AppCompatActivity {

  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";


  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);

    //ActivityMainBinding binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
    //FooIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.foo_index);
    //BarIndexBindingImpl binding = DataBindingUtil.setContentView(act, R.layout.test_zero_index);
    //TdiActivityMainBinding bindingTdi = DataBindingUtil.setContentView(act, com.geovra.tdi.R.layout.tdi_activity_main);

    //binding.msg.setText("tdi: appbar");

    this.setContentView(R.layout.test_one_index);










    final AppCompatActivity _act = this;
    final CollapsingToolbarLayout toolbar = this.findViewById(R.id.ctlCollaps);
    NestedScrollView scroll = (NestedScrollView) this.findViewById(R.id.nsvContent);

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

}



//// Since this is an object collection, use a FragmentStatePagerAdapter,
//// and NOT a FragmentPagerAdapter.
//public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
//  public DemoCollectionPagerAdapter(FragmentManager fm) {
//    super(fm);
//  }
//
//  @Override
//  public Fragmentt getItem(int i) {
//    Fragment fragment = new DemoObjectFragment();
//    Bundle args = new Bundle();
//    // Our object is just an integer :-P
//    args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
//    fragment.setArguments(args);
//    return fragment;
//  }
//
//  @Override
//  public int getCount() {
//    return 100;
//  }
//
//  @Override
//  public CharSequence getPageTitle(int position) {
//    return "OBJECT " + (position + 1);
//  }
//}
