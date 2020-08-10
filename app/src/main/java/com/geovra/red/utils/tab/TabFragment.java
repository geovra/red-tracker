package com.geovra.red.utils.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.filter.ui.FilterFragment;

public class TabFragment extends FilterFragment
{
  private RedActivity activity;

  protected int layoutFile;

  @Override
  // Inflate the layout for this fragment
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = null;

    if (layoutFile != 0) {

      view = inflater.inflate(layoutFile, container, false);
    }

    return view;
  }
}
