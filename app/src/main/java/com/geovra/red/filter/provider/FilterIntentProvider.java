package com.geovra.red.filter.provider;

import android.util.Log;

import com.geovra.red.app.provider.RedIntentProvider;
import com.geovra.red.app.ui.RedActivity;

class FilterIntentProvider extends RedIntentProvider {
  private static final String TAG = "FilterIntentProvider";
  // protected OptionsItemSelector optionsItemSelector = new OptionsItemSelector();

  public FilterIntentProvider(RedActivity ctx)
  {
    super(ctx);
  }


  public void boot()
  {
    super.boot();

    Log.d(TAG, "boot");

    // trigger(R.id.item_search)
    //   .between(DashboardActivity.class, ItemCreateUpdateActivity.class)
    //   .with((DashboardActivity source, ItemCreateUpdateActivity target)  -> {
    //     return new Payload()
    //       .addString("foo", from.getFoo())
    //       .addString("bar", "...")
    //       .addString("baz", "...");
    //   });
  }
}
