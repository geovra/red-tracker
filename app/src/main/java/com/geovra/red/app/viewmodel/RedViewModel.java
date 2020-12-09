package com.geovra.red.app.viewmodel;

import android.app.Application;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.geovra.red.R;

import java.util.ArrayList;

/**
 * ModelView class
 */
public class RedViewModel extends AndroidViewModel {

    public RedViewModel(@NonNull Application application) {
        super(application);
    }

}
