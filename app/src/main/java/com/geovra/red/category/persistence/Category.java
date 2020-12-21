package com.geovra.red.category.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

import static com.geovra.red.shared.list.SelectableRecyclerAdapter.ViewHolderInput;

@Entity(tableName = "category")
public class Category implements ViewHolderInput {
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String name;

  @Nullable
  @Getter @Setter
  public String description;

  public Category(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }

  @Override
  public String getText() {
    return name;
  }
}
