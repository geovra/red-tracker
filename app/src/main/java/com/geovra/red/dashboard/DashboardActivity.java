package com.geovra.red.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;
import androidx.transition.Visibility;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.RedService;
import com.geovra.red.database.RedDatabase;
import com.geovra.red.item.ItemService;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * View class
 */
public class DashboardActivity extends RedActivity {
    public DashboardPageAdapter adapter;
    public DashboardViewModel vm;
    public RedService sRed;
    public ItemService sItem;
    public TabLayout tabLayout;
    public ViewPager pager;

     @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);

        setToolbar();

        vm = ( new DashboardViewModel() ); // vm = ViewModelProviders.of(this).get(DashboardViewModel.class);
        sItem = ( new ItemService() );

        setViewPager();

        // RedDatabase db = Room.databaseBuilder( getApplicationContext(), RedDatabase.class, "red_v1" ).build();
        // ...
    }


    public void setViewPager()
    {
        pager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new DashboardPageAdapter( getSupportFragmentManager(),this, vm.getItems(), vm.getIntervalDays() );

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // tabLayout.setupWithViewPager(pager);

        setTabListener();
        setTabCurrentDay();
    }


    public void setTabListener() {
        final int tabTodayIndex = sItem.setTabs(LayoutInflater.from(this), tabLayout);
        final AppCompatActivity ctx = this;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    TabLayout.Tab tabToday = tabLayout.getTabAt(tabTodayIndex);
                    tabToday.getCustomView().setVisibility(View.GONE);
                    View view = sItem.getTabCustomView( LayoutInflater.from(ctx), (String) tab.getTag(), R.layout.tab_main_day_today, tab );
                    tabToday.setCustomView(view);
                } catch (Exception e) {}
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                try {
                    TabLayout.Tab tabToday = tabLayout.getTabAt(tabTodayIndex);
                    tabToday.getCustomView().setVisibility(View.GONE);
                    View view = sItem.getTabCustomView( LayoutInflater.from(ctx), (String) tab.getTag(), R.layout.tab_main_day_today, tab );
                    tabToday.setCustomView(view);
                } catch (Exception e) {}
                pager.setCurrentItem(tab.getPosition());
            }
        });
    }


    public void setTabCurrentDay()
    {
        int day = vm.getDayOfWeek() - 1;
        tabLayout.setScrollPosition(day,0f,true);
        pager.setCurrentItem(day);
    }


    protected void setToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main); // Find the toolbar view inside the activity layout
        setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return vm.onOptionsItemSelected(this, item);
    }

}

// LayoutParams layoutParams=new LayoutParams(int width, int height);
// layoutParams.setMargins(int left, int top, int right, int bottom);
// imageView.setLayoutParams(layoutParams);