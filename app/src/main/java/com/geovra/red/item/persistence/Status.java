package com.geovra.red.item.persistence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "status")
public class Status {
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String name;

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

  public Status(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
