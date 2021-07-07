package com.geovra.red.app.provider;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;

import com.geovra.red.app.ui.RedActivity;
import com.geovra.red.shared.Payload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class RedIntentProvider {
  @Getter
  private RedActivity ctx;
  private int lastInserted = -1;
  private Map<Integer, Action> actions = new HashMap<>();

  public RedIntentProvider(RedActivity ctx)
  {
    this.ctx = ctx;
  }


  public void boot()
  {
    //
  }


  public boolean handle(int id)
  {
    return actions.get(id).run();
  }


  public RedIntentProvider listenClick(int id, AdapterView.OnItemClickListener click)
  {
    actions.put(id, new ActionCallback(click));
    lastInserted = id;
    return this;
  }

  public RedIntentProvider createIntent(int id)
  {
    actions.put(id, new ActionIntent());
    lastInserted = id;
    return this;
  }


  public RedIntentProvider to(Class to)
  {
    actions.put(lastInserted, new ActionIntent(ctx, to));
    return this;
  }


  public Action data(DataBuilder builder)
  {
    Action intent = actions.get(lastInserted);
    Payload payload = builder.build(this);
    payload.fill(intent);
    return intent;
  }


  public interface Action {
    public boolean run();
    public void data(String key, String value);
  }


  public class ActionCallback implements Action {
    private AdapterView.OnItemClickListener listener;

    public ActionCallback(AdapterView.OnItemClickListener click)
    {
      this.listener = click;
    }

    public boolean run()
    {
      //
      return true;
    }

    public void data(String key, String value)
    {
      //
    }
  }


  public class ActionIntent implements Action {
    private Intent intent;

    public ActionIntent()
    {
      intent = new Intent();
    }

    public ActionIntent(Context ctx, Class<?> c)
    {
      intent = new Intent(ctx, c);
    }

    public boolean run()
    {
      //
      return true;
    }

    public void data(String key, String value)
    {
      intent.putExtra(key, value);
    }
  }


  public interface DataBuilder {
    public Payload build(RedIntentProvider intentProvider);
  }
}
