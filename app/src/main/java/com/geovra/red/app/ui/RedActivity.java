package com.geovra.red.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelStore;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.geovra.red.R;
import com.geovra.red.app.provider.RedIntentProvider;
import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.shared.bus.Bus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

public class RedActivity extends AppCompatActivity {
  private static String RED_ACTIVITY = "com.geovra.red.app.ui.RedActivity";
  private RedViewModel rvm;
  private ViewModelStore mViewModelStore;
  @Getter @Setter
  protected RedIntentProvider intentProvider;
  @Getter
  protected Bus.Disposable disposable = new Bus.Disposable();

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    intentProvider = this.newProvider();
  }


  private RedIntentProvider newProvider()
  {
    String fqcn = this.getClass().toString();
    String[] parts = fqcn.split("\\.");

    String nsBefore = parts.length > 0 ? parts[ parts.length - 1 ].replace("Activity", "") : "N/A";
    String ns = nsBefore.split("(?=\\p{Upper})")[1]; // Regexp with zero-width positive lookahead - it finds uppercase letters but doesn't include them into delimiter

    String providerClass = fqcn
      .replace("class ", "")
      .replace(".ui", ".provider")
      .replace("." + nsBefore + "Activity", "." + ns + "IntentProvider"); // DashboardIntentProvider

    // See RedIntentProvider::boot
    try {
      Log.i(RED_ACTIVITY, providerClass + "::boot");
      Class provider = Class.forName(providerClass);
      Constructor constructor = provider.getConstructor(RedActivity.class);
      Object instance = constructor.newInstance(this);
      Method boot = provider.getMethod("boot");
      boot.invoke(instance);

      return (RedIntentProvider) instance;
    }
    catch (NoSuchMethodException e) {
      Log.d(RED_ACTIVITY, "No such method for: " + providerClass);
    }
    catch (ClassNotFoundException e) {
      Log.d(RED_ACTIVITY, "No intent provider found for: " + providerClass);
    }
    catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return null;
  }


  @Override // Parent activity handles the menu activation and listeners; sub activities configure the actual menu to load
  public boolean onPrepareOptionsMenu(Menu menu)
  {
    // Menu icons are inflated just as they were with actionbar; this adds items to the action bar if it is present.
    int id = getOptionsMenu();
    if (id > 0) {
      getMenuInflater().inflate(id, menu);
    }
    return true;
  }


  public int getOptionsMenu()
  {
    return -1;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    boolean done = intentProvider.handle(item.getItemId());

    // switch (item.getItemId()) {
    //
    //   case R.id.item_add:
    //     vm.getItemService().toCreate(this, ItemCreateUpdateActivity.class);
    //     break;
    //   case R.id.item_search:
    //
        // case R.id.interval_next:
        //   Toast.show(this, "Interval next", Toast.LENGTH_LONG);
        //   break;
    // }

    // if (item.getItemId() == R.id.action_chat) {
    // ...
    // } else if (...) {...}

    return true;
  }


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
    return true;
  }


  protected int getOptionsMenuId() {
    return -1;
  }


  public <T extends RedActivity> void keyboardHide(IBinder token, T ctx) {
    InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(token, 0);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    Bus.dispose(getDisposable());
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
