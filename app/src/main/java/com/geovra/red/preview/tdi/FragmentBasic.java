package com.geovra.red.preview.tdi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.geovra.red.R;

public class FragmentBasic extends Fragment {

  public int mView;


  public FragmentBasic(int view) {
    this.mView = view;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(mView, container, false);
  }

}