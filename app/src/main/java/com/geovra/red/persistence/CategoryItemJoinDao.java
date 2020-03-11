package com.geovra.red.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.geovra.red.model.Category;
import com.geovra.red.model.CategoryItemJoin;
import com.geovra.red.model.Item;

import java.util.List;

@Dao
public interface CategoryItemJoinDao {

    @Insert
    void insert(CategoryItemJoin cit);

    @Query("SELECT * FROM categories " +
            "INNER JOIN categories_items_join " +
            "ON category.id=categories_items_join.category_id " +
            "WHERE categories_items_join.item_id=:item_id")
    List<Category> getCategoriesForItem(final int item_id);

    @Query("SELECT * FROM song " +
            "INNER JOIN playlist_song_join " +
            "ON song.id=playlist_song_join.songId " +
            "WHERE playlist_song_join.playlistId=:playlistId")
    List<Item> getSongsForPlaylist(final int playlistId);

}
