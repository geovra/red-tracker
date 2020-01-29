package com.geovra.red;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;


public class RedActivity extends AppCompatActivity {
  private static String TDI_APPLICATION = "com.geovra.tdi.TdiActivity";
  private RedViewModel rvm;


  public void onCreateFragment(RedActivity activity, View view) {}


  public void setViewModel(RedViewModel rvm) {
    this.rvm = rvm;
  }


  public RedViewModel getViewModel() {
    return rvm;
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
