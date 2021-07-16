package com.geovra.red.app.adapter;

import androidx.fragment.app.FragmentManager;

import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.item.ItemViewModel;
import com.geovra.red.item.ui.adapter.ItemPageAdapter;
import com.geovra.red.dashboard.DashboardViewModel;

public class DashboardPageAdapter extends ItemPageAdapter {
    private ItemViewModel itemViewModel;

    public DashboardPageAdapter(FragmentManager fm, RedActivity activity, DashboardViewModel vmDashboard, ItemViewModel itemViewModel)
    {
        super(fm, activity, vmDashboard, itemViewModel);
        this.itemViewModel = itemViewModel;
    }
}
