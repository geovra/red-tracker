package com.geovra.red.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.RedActivity;

public class ItemIndexFragment extends Fragment {
    private RedActivity activity;
    private ItemPageAdapter page;
    private String input; // string => "MONDAY"

    public ItemIndexFragment(ItemPageAdapter page, String input /* , RedActivity activity */)
    {
        // this.activity = activity;
        this.input = input;
        this.page = page;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.data_item_list_basic, container, false);

        // Initialize recycler
        setListView(view);

        return view;
    }


    public void setListView(View view)
    {
        // RecyclerView setup
        RecyclerView recyclerView = view.findViewById(R.id.item_rv);

        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()) );
        ItemRecycleAdapter adapter = new ItemRecycleAdapter( getContext(), page.getData() );

        // ItemRecycleAdapter adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);
    }

}
