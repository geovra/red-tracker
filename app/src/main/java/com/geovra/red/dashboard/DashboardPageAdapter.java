package com.geovra.red.dashboard;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.geovra.red.R;
import com.geovra.red.RedActivity;
import com.geovra.red.RedViewModel;
import com.geovra.red.item.ItemPageAdapter;

import java.util.ArrayList;

public class DashboardPageAdapter extends ItemPageAdapter {

    public DashboardPageAdapter(FragmentManager fm, RedActivity activity, ArrayList<String> items, ArrayList<String> intervalDays)
    {
        super(fm, activity, items, intervalDays);
    }

}
