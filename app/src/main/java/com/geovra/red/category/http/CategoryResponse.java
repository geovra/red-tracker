package com.geovra.red.category.http;

import androidx.annotation.NonNull;

import com.geovra.red.category.persistence.Category;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class CategoryResponse {

  public static class CategoryIndex {
    @Getter @Setter List<Category> data;

    @NonNull
    @Override
    public String toString() {
      return data.size() > 0
          ? "[{" + data.get(0).getName() + "}, ...]"
          : "[empty]";
    }
  }
}
