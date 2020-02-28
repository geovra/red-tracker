package com.geovra.red.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public String date;

    @Getter @Setter
    @ColumnInfo(name = "created_at")
    public String createdAt;

    @Getter @Setter
    @ColumnInfo(name = "updated_at")
    public String updatedAt;

    public String newLinesApply(String str) {
        return str.replace("\\n", "\n");
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + title + "}";
    }
}
