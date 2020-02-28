package com.geovra.red.adapter;

import androidx.fragment.app.FragmentManager;

import com.geovra.red.RedActivity;
import com.geovra.red.adapter.item.ItemPageAdapter;
import com.geovra.red.viewmodel.DashboardViewModel;

import java.util.ArrayList;

public class DashboardPageAdapter extends ItemPageAdapter {

    public DashboardPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard, ArrayList<String> intervalDays)
    {
        super(fm, activity, vmDashboard, intervalDays);
    }

}
