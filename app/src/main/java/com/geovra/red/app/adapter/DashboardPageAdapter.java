package com.geovra.red.app.adapter;

import androidx.fragment.app.FragmentManager;

import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.adapter.ItemPageAdapter;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;

import java.util.ArrayList;

public class DashboardPageAdapter extends ItemPageAdapter {

    public DashboardPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard)
    {
        super(fm, activity, vmDashboard);
    }

}
