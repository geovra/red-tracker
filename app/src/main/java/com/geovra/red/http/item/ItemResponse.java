package com.geovra.red.http.item;

import androidx.annotation.NonNull;

import com.geovra.red.model.Item;

import java.util.List;

import lombok.*;

public class ItemResponse {

  public static class ItemIndex {
    @Getter @Setter List<Item> data;

    @NonNull
    @Override
    public String toString() {
      return data.size() > 0
          ? "[{" + data.get(0).getTitle() + "}, ...]"
          : "[empty]";
    }
  }

  public static class ItemStore {
    @Getter @Setter String id;
    @Getter @Setter String title;
    @Getter @Setter String description;

    public String toString() {
      return String.format("{%s}", getTitle());
    }

  }
}

