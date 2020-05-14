package com.geovra.red.item.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.geovra.red.item.persistence.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories")
    List<Category> getAll();

    @Query("SELECT * FROM categories WHERE id IN (:categoryIds)")
    List<Category> loadAllByIds(int[] categoryIds);

    @Query("SELECT * FROM categories WHERE name LIKE :name LIMIT 1")
    Category findByTitle(String name);

    @Insert
    void insertAll(Category... categories);

    @Delete
    void delete(Category category);

}
