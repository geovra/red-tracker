package com.geovra.red.model.item;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

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
    public String status;

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

    @NonNull
    @Override
    public String toString() {
        return "{" + title + ", " + date + "}";
    }
}
