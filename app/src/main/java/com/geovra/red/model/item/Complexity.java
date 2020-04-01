package com.geovra.red.model.item;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.geovra.red.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "complexity")
public class Complexity {
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String name;

  @Nullable
  @Getter @Setter
  public String description;

  public Complexity(int id, String name) {
    this.id = id;
    this.name = name;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
