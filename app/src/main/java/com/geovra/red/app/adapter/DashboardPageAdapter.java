package com.geovra.red.app.adapter;

import androidx.fragment.app.FragmentManager;

import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.ui.adapter.ItemPageAdapter;
import com.geovra.red.dashboard.viewmodel.DashboardViewModel;

public class DashboardPageAdapter extends ItemPageAdapter {
    public DashboardPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard)
    {
        super(fm, activity, vmDashboard);
    }
}
