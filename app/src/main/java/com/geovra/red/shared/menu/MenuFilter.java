package com.geovra.red.shared.menu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.R;
import com.geovra.red.filter.service.FilterService;
import com.geovra.red.filter.ui.FilterIndexActivity;
import com.geovra.red.item.ui.ItemCreateUpdateActivity;

public class MenuFilter
{
  public static int ACTIVITY_REQUEST_CODE = 1;

  public boolean onOptionsItemSelected(AppCompatActivity ctx, FilterService filterService, MenuItem item)
  {
    switch (item.getItemId()) {

      case R.id.filter_permanent_store:
        filterService.permanentFilterStore();
        break;
    }

    return true;
  }

}
