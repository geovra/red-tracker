package com.geovra.red.item.persistence;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.geovra.red.item.persistence.Category;
import com.geovra.red.item.persistence.Item;

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
