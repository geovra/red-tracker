package com.geovra.red.category;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Insert;
import androidx.room.Query;

import com.geovra.red.item.Item;

import java.util.List;

@Entity(tableName = "category_item_join",
        primaryKeys = { "category_id", "item_id" },
        foreignKeys = {
            @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id"),
            @ForeignKey(entity = Item.class,
                parentColumns = "id",
                childColumns = "category_id")
        })
public class CategoryItemJoin {
    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "item_id")
    public int itemId;
}
