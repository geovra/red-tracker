package com.geovra.red.filter.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.geovra.red.app.persistence.RedPrefs;
import com.geovra.red.app.viewmodel.RedViewModel;
import com.geovra.red.category.persistence.Category;
import com.geovra.red.category.persistence.CategoryRepo;
import com.geovra.red.category.ui.FilterCategoryFragment;
import com.geovra.red.filter.persistence.FilterOutput;
import com.geovra.red.filter.service.FilterService;
import com.geovra.red.filter.ui.FilterIntervalFragment;
import com.geovra.red.shared.date.DateService;
import com.geovra.red.status.persistence.Status;
import com.geovra.red.status.service.StatusService;
import com.geovra.red.status.ui.FilterStatusFragment;
import com.google.gson.Gson;

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

  @Getter @Setter private MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
  @Getter @Setter private MutableLiveData<List<Category>> categorySelected = new MutableLiveData<>();

  @Getter @Setter private MutableLiveData<FilterOutput> filterPermanent = new MutableLiveData<>();

  @Getter @Setter private FilterService filterService;
  @Getter @Setter private StatusService statusService;
  @Getter @Setter private CategoryRepo categoryRepo;
  @Getter @Setter private DateService dateService;
  @Getter @Setter private List<Status> dataSelected = new ArrayList<>();

  /**
   * TODO: Add inversion of control using Hilt or similar.
   *
   * @param application
   */
  public FilterViewModel(@NonNull Application application)
  {
    super(application);

    mPages = new ArrayList<>();
    mPages.add(new FilterIntervalFragment(this));
    mPages.add(new FilterStatusFragment(this));
    mPages.add(new FilterCategoryFragment(this));

    filterService = new FilterService();
    statusService = new StatusService(getApplication().getBaseContext());
    categoryRepo = new CategoryRepo(getApplication().getBaseContext());
    dateService = new DateService();

    dateFrom = dateService.getToday();
    dateTo = dateService.getTomorrow();

    statusList.setValue(new ArrayList<>());
    statusSelected.setValue(new ArrayList<>());
    filterPermanent.setValue(new FilterOutput());

    categoryList.setValue(new ArrayList<>());
    categorySelected.setValue(new ArrayList<>());
  }


  public FilterOutput permanentFilterCheck(Context ctx)
  {
    filterPermanent.setValue(
      new Gson().fromJson(
        new RedPrefs().getString(ctx, "FILTERS_PERMANENT"),
        FilterOutput.class
      )
    );
    return filterPermanent.getValue();
  }


  public void permanentFilterStore()
  {
  }


  public void permanentFilterDelete()
  {
  }

}
