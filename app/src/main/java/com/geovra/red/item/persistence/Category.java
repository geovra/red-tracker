package com.geovra.red.item.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "category")
public class Category {
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
}
