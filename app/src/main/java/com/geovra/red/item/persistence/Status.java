package com.geovra.red.item.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "status")
public class Status implements SelectableRecyclerAdapter.ViewHolderInput {
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String name;

  @Getter @Setter
  public String color;

  @Nullable
  @Getter @Setter
  public String description;

  @Getter @Setter
  @SerializedName("created_at")
  @ColumnInfo(name = "created_at")
  public String createdAt;

  @Getter @Setter
  @ColumnInfo(name = "updated_at")
  @SerializedName("updated_at")
  public String updatedAt;

  public Status(String name)
  {
    this.name = name;
  }

  public Status(int id, String name)
  {
    this.id = id;
    this.name = name;
  }

  public Status(int id, String name, String color)
  {
    this.id = id;
    this.name = name;
    this.color = color;
  }

  @NonNull
  @Override
  public String toString()
  {
    return String.format("{name: %s}", name);
  }

  @Override
  public String getText() {
    return name;
  }
}
