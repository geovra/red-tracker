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

@Entity(tableName = "items")
public class Item {
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String title;

  @Nullable
  @Getter @Setter
  public String description;

  @Getter @Setter
  public int status;

  @Getter @Setter
  @SerializedName("is_continuous")
  @ColumnInfo(name = "is_continuous")
  public String isContinuous;

  @Getter @Setter
  public String date;

  @Getter @Setter
  @SerializedName("created_at")
  @ColumnInfo(name = "created_at")
  public String createdAt;

  @Getter @Setter
  @ColumnInfo(name = "updated_at")
  @SerializedName("updated_at")
  public String updatedAt;

  public String newLinesApply(String str) {
    return str.replace("\\n", "\n");
  }

  public String getCreatedAtReadable(Context ctx) {
    try {
      return DateUtils.getTimeString(ctx, new SimpleDateFormat("yyyy-MM-dd").parse(createdAt));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String getTitleReadable()
  {
    String output = "";
    String[] pieces = title.split(" ");
    int length = 0, lengthAllowed = 148;
    for (String p : pieces) {
      length += p.length();
      if (length > lengthAllowed) { break; }
      output += (p + " ");
    }
    return output;
  }

  public String getStatusReadable(Context ctx) {
    try {
      int id = ctx.getResources().getIdentifier("status_" + status, "string", "com.geovra.red");
      return ctx.getResources().getString(id);
    } catch (Exception e) {
      return "Unknown";
    }
  }

  @NonNull
  @Override
  public String toString() {
    return "{" + title + ", " + date + "}";
  }
}
