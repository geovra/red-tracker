package com.geovra.red.filter.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.geovra.red.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Field;

public class FilterPermanentToggleBottomSheet extends BottomSheetDialogFragment
{

  private final BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
      if (newState == BottomSheetBehavior.STATE_HIDDEN) {
        dismiss();
      }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
    }
  };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
    View contentView = View.inflate(getContext(), R.layout.filter_permanent_dialog, null);
    dialog.setContentView(contentView);

    // CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
    // CoordinatorLayout.Behavior<View> behavior = layoutParams.getBehavior();
    // if (behavior instanceof BottomSheetBehavior) {
    //   ((BottomSheetBehavior<View>) behavior).addBottomSheetCallback(mBottomSheetBehaviorCallback);
    //   ((BottomSheetBehavior<View>) behavior).setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    //   ((BottomSheetBehavior<View>) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
    // }

    setupFullHeight((BottomSheetDialog) dialog);

    return dialog;
  }


  private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
    FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
    bottomSheet.setBackgroundColor((int) R.color.redPrimary);

    // Calculate window height for fullscreen use
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    int windowHeight = displayMetrics.heightPixels;

    // ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
    // if (layoutParams != null) {
    //   layoutParams.height = windowHeight;
    // }
    // bottomSheet.setLayoutParams(layoutParams);

    // behavior.setFitToContents(false);
    // behavior.setHalfExpandedRatio(0.4f);
    // behavior.setPeekHeight((int) (windowHeight * 0.4));
    // behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    // behavior.setDraggable(false);
  }

}
