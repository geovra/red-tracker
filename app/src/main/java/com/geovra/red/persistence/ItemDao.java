package com.geovra.red.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.geovra.red.model.item.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM Category")
    List<Item> getAll();

    @Query("SELECT * FROM Category WHERE uid IN (:userIds)")
    List<Item> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Category WHERE title LIKE :title LIMIT 1")
    Item findByTitle(String title);

    @Insert
    void insertAll(Item... items);

    @Delete
    void delete(Item item);

}
