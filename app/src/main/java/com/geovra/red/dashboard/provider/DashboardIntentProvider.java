package com.geovra.red.dashboard.provider;

import android.content.Intent;
import android.util.Log;

import com.geovra.red.R;
import com.geovra.red.app.provider.RedIntentProvider;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.dashboard.ui.DashboardActivity;
import com.geovra.red.item.ui.ItemCreateUpdateActivity;
import com.geovra.red.shared.Payload;

class DashboardIntentProvider extends RedIntentProvider {
  private static final String TAG = "DashboardIntentProvider";
  // protected OptionsItemSelector optionsItemSelector = new OptionsItemSelector();

  public DashboardIntentProvider(RedActivity ctx)
  {
    super(ctx);
  }


  public void boot()
  {
    super.boot();
    Log.d(TAG, "boot");

    createIntent(R.id.item_filter)
      .to(ItemCreateUpdateActivity.class)
      .data((RedIntentProvider current) -> {
        Payload payload = new Payload();
        payload.put("foo", current.getCtx().toString());
        payload.put("bar", "...");
        payload.put("baz", "...");
        return payload;
      });

    listenClick(R.id.item_add, (a, b, c, d) -> {
      //
    });
  }
}
