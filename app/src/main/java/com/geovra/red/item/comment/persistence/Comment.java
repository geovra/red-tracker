package com.geovra.red.item.comment.persistence;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.geovra.red.shared.list.SelectableRecyclerAdapter;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "comments")
public class Comment implements SelectableRecyclerAdapter.ViewHolderInput
{
  @Getter @Setter
  @PrimaryKey
  public int id;

  @Getter @Setter
  public String body;

  @Getter @Setter
  @SerializedName("created_at")
  @ColumnInfo(name = "created_at")
  public String createdAt;

  @Getter @Setter
  @ColumnInfo(name = "updated_at")
  @SerializedName("updated_at")
  public String updatedAt;


  public Comment() {
  }


  public Comment(String body) {
    this.body = body;
  }


  @NonNull
  @Override
  public String toString() {
    return "{" + body + "}";
  }


  @Override
  public String getText() {
    return body;
  }
}
