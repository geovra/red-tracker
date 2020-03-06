package com.geovra.red.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SingletonViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  private DashboardViewModel vm;

  private final Map<Class<? extends ViewModel>, ViewModel> mHash = new HashMap<>();

  public SingletonViewModelFactory(DashboardViewModel vm) {
    this.vm = vm;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(final @NonNull Class<T> modelClass) {
    mHash.put(modelClass, vm);

    if (DashboardViewModel.class.isAssignableFrom(modelClass)) {
      DashboardViewModel sharedVM = null;

      if (mHash.containsKey(modelClass)) {
        sharedVM = (DashboardViewModel) mHash.get(modelClass);
      } else {
        try {

          sharedVM = (DashboardViewModel) modelClass.getConstructor(Runnable.class).newInstance(new Runnable() {
            @Override
            public void run() {
              mHash.remove(modelClass);
            }
          });

        } catch (NoSuchMethodException e) {
          throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InstantiationException e) {
          throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }

        mHash.put(modelClass, sharedVM);
      }

      return (T) sharedVM;
    }

    return super.create(modelClass);
  }

}