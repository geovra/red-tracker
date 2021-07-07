package com.geovra.red.shared.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.R;
import com.geovra.red.filter.ui.FilterIndexActivity;
import com.geovra.red.item.ui.ItemCreateUpdateActivity;

import java.util.function.Function;

public class MenuMain
{
  public static int ACTIVITY_REQUEST_CODE = 1;

  public boolean onOptionsItemSelected(AppCompatActivity ctx, MenuService menuService, MenuItem item)
  {
    switch (item.getItemId()) {

      case R.id.item_add:
        menuService.toActivity(ctx, ItemCreateUpdateActivity.class, -1);
        break;

      case R.id.item_filter:
        menuService.toActivity(ctx, FilterIndexActivity.class, ACTIVITY_REQUEST_CODE);
        break;
    }

    return true;
  }


  public static interface BundleCallback {
    public Bundle apply(Bundle bundle);
  }

}
