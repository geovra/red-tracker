package com.geovra.red.filter.viewmodel;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.filter.ui.FilterCategoryFragment;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.filter.ui.FilterStatusFragment;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@SuppressLint("CheckResult")
public class FilterViewModel extends RedViewModel {
  @Getter @Setter
  private List<Fragment> mPages;

  public FilterViewModel() {
    mPages = new ArrayList<>();
    mPages.add(new FilterIntervalFragment());
    mPages.add(new FilterStatusFragment());
    mPages.add(new FilterCategoryFragment());
  }
}