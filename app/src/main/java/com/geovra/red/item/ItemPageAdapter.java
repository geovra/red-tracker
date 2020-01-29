package com.geovra.red.item;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.geovra.red.RedActivity;
import com.geovra.red.RedViewModel;

import java.util.ArrayList;
import java.util.List;

public class ItemPageAdapter extends FragmentStatePagerAdapter {
    private int tabNumber;
    private ArrayList<String> data;
    private RedActivity activity;
    private ArrayList<String> items;
    private ArrayList<String> intervalDays;

    public ItemPageAdapter(FragmentManager fm, RedActivity activity, ArrayList<String> items, ArrayList<String> intervalDays)
    {
        super(fm);
        this.activity = activity;
        this.tabNumber = intervalDays.size();
        this.intervalDays = intervalDays;
        this.items = items;
    }


    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        // String item = items.get(0);
        String day = intervalDays.get(position);
        ItemIndexFragment fragment = new ItemIndexFragment(this, day);

        return fragment;
    }


    @Override
    public int getCount() {
        return tabNumber;
    }


    public ArrayList<String> getData() {
        return items;
    }


    public void setData(ArrayList<String> list) {
        this.items = list;
    }

}
