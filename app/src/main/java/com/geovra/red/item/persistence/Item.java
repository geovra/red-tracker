package com.geovra.red.item.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.geovra.red.shared.DateUtils;
import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "items")
public class Item implements SelectableRecyclerAdapter.ViewHolderInput {
  public static int STATUS_COMPLETED = 0x0;
  public static int STATUS_PENDING = 0x9;
  public static int STATUS_ADDED = 0x8;
  public static int STATUS_POSTPONED = 0x7;
  public static int STATUS_HUH = 0x6;
  public static int STATUS_URGENT = 0x5;

  public static int COMPLEXITY_OK = 0x0;
  public static int COMPLEXITY_DOABLE = 0x9;
  public static int COMPLEXITY_FUCK = 0x8;
  public static int COMPLEXITY_HARD = 0x7;

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
  @ColumnInfo(name = "complexity")
  public int complexity;

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

  public Item() {
  }

  public Item(String title) {
    this.title = title;
  }

  public String newLinesApply(String str) {
    return str.replace("\\n", "\n");
  }

  public String getCreatedAtReadable(Context ctx) {
    try {
      return DateUtils.getTimeString(ctx, new SimpleDateFormat("yyyy-MM-dd").parse(createdAt));
    } catch (Exception e) {
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
      int id = ctx.getResources().getIdentifier("status_" + String.valueOf(status), "string", "com.geovra.red");
      return ctx.getResources().getString(id);
    } catch (Exception e) {
      return "Unknown";
    }
  }


  public String getComplexityReadable(Context ctx) {
    try {
      int id = ctx.getResources().getIdentifier("complexity_" + complexity, "string", "com.geovra.red");
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


  @Override
  public String getText() {
    return title;
  }
}
