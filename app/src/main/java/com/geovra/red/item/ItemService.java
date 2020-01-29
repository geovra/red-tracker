package com.geovra.red.item;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.geovra.red.R;
import com.geovra.red.RedContext;
import com.geovra.red.RedService;
import com.geovra.red.dashboard.DashboardViewModel;
import com.google.android.material.tabs.TabLayout;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemService {
  public RedService sRed;

  public ItemService() {
    sRed = new RedService();
    HttpUrl.Builder url = new HttpUrl.Builder();
    url.scheme("http");
    url.host("dev-tracker.acme");
    url.addPathSegment("api");
    url.addPathSegment("v1");
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.50.101/")
        // .baseUrl(url.build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    retrofit.create(ItemApi.class).getItems(null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            this::handleResults,
            error -> System.out.println("Error"),
            () -> System.out.println(200)
        );
  }


  private void handleResults(ItemIndexResponse res) {
    System.out.println("zzz 200 OK - results");
//    if(res.getCoupons() != null){
//      CouponsAdapter ca = new CouponsAdapter(storeCoupons.getCoupons(), MainActivity.this);
//      couponRecyclerView.setAdapter(ca);
//    }else{
//      TextView store_name = (TextView) findViewById(R.id.store_name);
//      store_name.setText(storeCoupons.getStore());
//      TextView coupon_count = (TextView) findViewById(R.id.coupon_count);
//      coupon_count.setText(storeCoupons.getTotalCoupons());
//      TextView max_cashback = (TextView) findViewById(R.id.max_cashback);
//      max_cashback.setText(storeCoupons.getMaxCashback());
//    }
  }


  public int setTabs(LayoutInflater inflater, TabLayout tabLayout)
  {
    // mv.getInterval => [ "2019-08-11", "2019-08-12", "2019-08-13", ... ]
    // repo.getItems => [ {0}, {1}, {2}, {3}, {4}, {5}, ... ]

    int tabTodayIndex = -1;
    final String today = sRed.getToday();
    ArrayList<String> days = sRed.getIntervalDays();

    for (int i = 0; i < days.size(); i++) {
      System.out.println("zzz " + today + days.toString());
      TabLayout.Tab tab = tabLayout.newTab();

      boolean isToday = today.equals(days.get(i));
      int resId = isToday ? R.layout.tab_main_day : R.layout.tab_main_day_0;
      if (isToday) { tabTodayIndex = i; }

      View view = getTabCustomView( inflater, days.get(i), resId, null );
      tab.setCustomView(view);
      tab.setTag(today);

      tabLayout.addTab(tab);
    }

    return tabTodayIndex;
  }


  public View getTabCustomView(LayoutInflater inflater, String day /* dd-MM-YYYY */, int layoutId, TabLayout.Tab tab)
  {
    // final LayoutInflater inflater = LayoutInflater.from(ctx);
    View view = inflater.inflate(layoutId, null);
    Pair<String, String> info = getTabInformation(day);

    TextView txName = (TextView) view.findViewById(R.id.int_day_name);
    TextView txNum = (TextView) view.findViewById(R.id.int_day_num);
    txName.setText( info.first );
    txNum.setText( info.second );

    return view;
  }


  public Pair<String, String> getTabInformation(String date /* dd-MM-YYYY */)
  {
    String e = sRed.getDayOfWeek(date);
    String d = sRed.getDayOfMonth(date);
    return new Pair<>(e, d);
  }
}
