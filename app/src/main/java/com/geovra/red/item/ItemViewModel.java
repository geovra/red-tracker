package com.geovra.red.item;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.geovra.red.R;
import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.item.persistence.Item;

@SuppressLint("CheckResult")
public class ItemViewModel extends RedViewModel {
  private static final String TAG = "ItemViewModel";

  public ItemViewModel(@NonNull Application application) {
    super(application);
  }


  public Pair<Integer, Integer> setItemStatus(ImageView img, Resources resources, int status, int complexity)
  {
    int background = R.drawable.shape_circle;
    String sShape = "shape_circle";
    String sColor = "default";

    if (complexity == Item.COMPLEXITY_HARD) {
      background = R.drawable.shape_rect;
      sShape = "shape_rect";
    }

    if (complexity == Item.COMPLEXITY_FUCK) {
      background = R.drawable.shape_triangle;
      sShape = "shape_triangle";

      // ViewGroup.LayoutParams params = img.getLayoutParams();
      // float factor = resources.getDisplayMetrics().density;
      // params.width = (int) (28 * factor);
      // params.height = (int) (34 * factor);
      // img.setTranslationX(4 * factor);
      // img.setTranslationY(-12 * factor);
      // if (img.getId() == R.id.status_img) { // Hack alert, fuuuck!
      //   img.setTranslationX(-2 * factor);
      //   img.setTranslationY(-13 * factor);
      // }
      // img.setLayoutParams(params);
    }

    // Colors
    int color = R.color.yellowPrimary;
    if (true) {
      if (status == Item.STATUS_URGENT) {
        color = R.color.redPrimary;
        sColor = "redPrimary";
      }

      if (status == Item.STATUS_HUH) {
        color = R.color.bluePrimary;
        sColor = "bluePrimary";
      }

      if (status == Item.STATUS_POSTPONED) {
        color = R.color.tonePrimary;
        sColor = "tonePrimary";
      }

      if (status == Item.STATUS_ADDED) {
        color = R.color.greyDimmer;
        sColor = "greyDimmer";
      }

      if (status == Item.STATUS_COMPLETED) {
        color = R.color.greenPrimary;
        sColor = "greenPrimary";
      }
    }

    img.setImageDrawable(resources.getDrawable(background));
    // img.setBackground(resources.getDrawable(background));
    // img.setImageResource(background);
    img.setColorFilter(resources.getColor(color), PorterDuff.Mode.SRC_IN);

    // The second approach is always the best for this task. But still, if you want to go with the first approach then the correct way to use it is like this
    // int sdk = android.os.Build.VERSION.SDK_INT;
    // if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
    //   setBackgroundDrawable();
    // } else {
    //   setBackground();
    // }

    return new Pair<>(background, color);
  }


  public String setItemStatus(TextView text, Resources resources, Item item)
  {
    String name = null;
    try {
      int id = resources.getIdentifier("status_" + String.valueOf(item.getStatus()), "string", "com.geovra.red");
      name = resources.getString(id);
    } catch (Exception e) {
      Log.d(TAG, e.getMessage());
    }
    return name;
  }
}
