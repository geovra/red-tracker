package com.geovra.red;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.geovra.red.bus.Bus;
import com.geovra.red.ui.item.ItemShowActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;


public class RedActivity extends AppCompatActivity {
  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";
  private RedViewModel rvm;
  private ViewModelStore mViewModelStore;
  @Getter
  protected Bus.Disposable disposable = new Bus.Disposable();

  public void onCreateFragment(RedActivity activity, View view) {}


  public void setViewModel(RedViewModel rvm) {
    this.rvm = rvm;
  }


  public RedViewModel getViewModel() {
    return rvm;
  }


  protected void setToolbar(Integer idToolbar)
  {
    idToolbar = idToolbar == null ? R.id.toolbar_main : idToolbar;
    Toolbar toolbar = (Toolbar) findViewById(idToolbar); // Find the toolbar view inside the activity layout
    setSupportActionBar(toolbar); // Sets the Toolbar to act as the ActionBar for this Activity window. Make sure the toolbar exists in the activity and is not null
    // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // getSupportActionBar().setHomeButtonEnabled(true);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    int menuId = getOptionsMenuId();
    // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.menu_main, menu);
    // menu.clear();

    // menu.add(0, 1, 1, "Option A");
    // MenuItem mi1 = menu.findItem(1);
    // mi1.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    // mi1.setIcon(R.mipmap.ic_action_achievement_white);

    // String[] pieces = ItemShowActivity.class.toString().split(".");
    // Log.d(TAG, pieces[(pieces.length - 1)]);
    // menu.add(0, 2, 2, "Option B");

    return true;
  }


  protected int getOptionsMenuId() {
    return -1;
  }


  public <T extends RedActivity> void keyboardHide(IBinder token, T ctx) {
    InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(token, 0);
  }


  /*
  @Override
  protected void onCreate(Bundle state) {
    super.onCreate(state);

    // Delegate to the gateway
    ( new RedGateway() ).onCreate(this, state);

  }
  */

}
