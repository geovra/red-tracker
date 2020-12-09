package com.geovra.red.filter.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.filter.ui.FilterCategoryFragment;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.filter.ui.FilterStatusFragment;
import com.geovra.red.shared.date.DateService;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@SuppressLint("CheckResult")
public class FilterViewModel extends RedViewModel {
  @Getter @Setter private List<Fragment> mPages;
  @Getter @Setter private String dateFrom = "0000-00-00";
  @Getter @Setter private String dateTo = "0000-00-00";

  public FilterViewModel(@NonNull Application application)
  {
    super(application);

    mPages = new ArrayList<>();
    mPages.add(new FilterStatusFragment(this));
    mPages.add(new FilterIntervalFragment(this));
    mPages.add(new FilterCategoryFragment(this));

    DateService dateService = new DateService();
    dateFrom = dateService.getToday();
    dateTo = dateService.getTomorrow();
  }
}
