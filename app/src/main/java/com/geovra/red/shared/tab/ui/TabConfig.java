package com.geovra.red.shared.tab.ui;

import lombok.Getter;

public class TabConfig {
  @Getter
  private int viewHolderLayoutId;

  public TabConfig withViewHolderLayout(int layoutId) {
    viewHolderLayoutId = layoutId;
    return this;
  }
}
