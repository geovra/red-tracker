package com.geovra.red.filter.ui;

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
import com.geovra.red.filter.viewmodel.FilterViewModel;
import com.geovra.red.item.adapter.StatusRecyclerAdapter;
import com.geovra.red.item.http.StatusResponse;
import com.geovra.red.item.persistence.Status;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.Selectable;
import com.geovra.red.shared.list.SelectableRecyclerAdapter.ViewHolderInput;
import com.geovra.red.shared.tab.TabTitle;

import java.util.List;

@SuppressWarnings("CheckResult")
public class FilterStatusFragment extends Fragment implements TabTitle {
  private static final String TAG = "FilterStatusFragment";
  private RedActivity activity;
  private FilterViewModel vmFilter;

  RecyclerView recyclerView;
  StatusRecyclerAdapter<Status> adapter;

  public FilterStatusFragment(FilterViewModel vmFilter) {
    this.vmFilter = vmFilter;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.filter_status, container, false);

    recyclerView = view.findViewById(R.id.status_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter = new StatusRecyclerAdapter<Status>(getResources(), new Selectable() {
      @Override
      public List<? extends ViewHolderInput> getSelected() {
        return vmFilter.getStatusSelected().getValue();
      }

      @Override
      public List<? extends ViewHolderInput> getSelectable() {
        return vmFilter.getStatusList().getValue();
      }

      @Override
      public void onSelect(ViewHolderInput input) {
        vmFilter.getStatusSelected().getValue().add((Status) input);
      }

      @Override
      public void onDeselect(ViewHolderInput input) {
        vmFilter.getStatusSelected().getValue().remove((Status) input);
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

    vmFilter.getStatusService().findAll(getContext())
      .subscribe(
        res -> {
          List<Status> data = res.body().getData();
          Log.d(TAG, data.toString());

          adapter.changeDataSet(data);
          vmFilter.getStatusList().setValue(data);
          vmFilter.getStatusService().getCache().set(
            getContext(), "TABLE_STATUS", res.body(), StatusResponse.StatusIndex.class
          );
        },
        error -> {
          Log.d(TAG, error.toString());
          error.printStackTrace();
        },
        () -> Log.d(TAG, "200 readItems")
      );
  }


  public CharSequence getPageTitle() {
    return "Status";
  }
}
