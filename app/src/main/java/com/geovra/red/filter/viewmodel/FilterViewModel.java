package com.geovra.red.filter.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.filter.ui.FilterCategoryFragment;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.filter.ui.FilterStatusFragment;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.item.service.StatusService;
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

  @Getter @Setter private MutableLiveData<List<Status>> statusList = new MutableLiveData<List<Status>>();
  @Getter @Setter private MutableLiveData<List<Status>> statusSelected = new MutableLiveData<List<Status>>();

  @Getter @Setter private StatusService statusService;
  @Getter @Setter private DateService dateService;
  @Getter @Setter private List<Status> dataSelected = new ArrayList<>();

  public FilterViewModel(@NonNull Application application)
  {
    super(application);

    mPages = new ArrayList<>();
    mPages.add(new FilterStatusFragment(this));
    mPages.add(new FilterIntervalFragment(this));
    mPages.add(new FilterCategoryFragment(this));

    statusService = new StatusService();
    dateService = new DateService();
    dateFrom = dateService.getToday();
    dateTo = dateService.getTomorrow();

    statusList.setValue(new ArrayList<>());
    statusSelected.setValue(new ArrayList<>());
  }
}
