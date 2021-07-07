package com.geovra.red.category.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geovra.red.R;
import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.category.ui.adapter.CategoryRecyclerAdapter;
import com.geovra.red.category.http.CategoryResponse;
import com.geovra.red.category.persistence.Category;
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.Selectable;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.ViewHolderInput;
import com.geovra.red.shared.tab.TabTitle;

import java.util.List;

@SuppressWarnings("CheckResult")
public class FilterCategoryFragment extends Fragment implements TabTitle {
  private static final String TAG = "FilterCategoryFragment";
  private RedActivity activity;
  private FilterViewModel vmFilter;

  RecyclerView recyclerView;
  CategoryRecyclerAdapter<Category> adapter;

  public FilterCategoryFragment(FilterViewModel vmFilter) {
    this.vmFilter = vmFilter;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.filter_category, container, false);

    recyclerView = view.findViewById(R.id.category_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new CategoryRecyclerAdapter<>(getResources(), new Selectable() {
      @Override
      public List<? extends ViewHolderInput> getSelected() {
        return vmFilter.getCategorySelected().getValue();
      }

      @Override
      public List<? extends ViewHolderInput> getSelectable() {
        return vmFilter.getCategoryList().getValue();
      }

      @Override
      public void onSelect(ViewHolderInput input) {
        vmFilter.getCategorySelected().getValue().add((Category) input);
      }

      @Override
      public void onDeselect(ViewHolderInput input) {
        vmFilter.getCategorySelected().getValue().remove((Category) input);
      }
    });
    recyclerView.setAdapter(adapter);

    return view;
  }


  /**
   * Best practice to access members on the underlying activity (vmFilter)
   *
   * @param savedInstanceState
   */
  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    vmFilter.getCategoryService().findAll(getContext())
      .subscribe(
        res -> {
          List<Category> data = res.body().getData();
          Log.d(TAG, data.toString());

          adapter.changeDataSet(data);
          vmFilter.getCategoryList().setValue(data);
          vmFilter.getCategoryService().getCache().set(
            getContext(), "TABLE_CATEGORIES", res.body(), CategoryResponse.CategoryIndex.class
          );
        },
        error -> {
          Log.d(TAG, error.toString());
          error.printStackTrace();
        },
        () -> Log.d(TAG, "200 findAll")
      );
  }


  public CharSequence getPageTitle() {
    return "Category";
  }
}
