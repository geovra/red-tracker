package com.geovra.red.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geovra.red.model.item.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class RedDatabase extends RoomDatabase {

  public abstract ItemDao itemDao();

}
