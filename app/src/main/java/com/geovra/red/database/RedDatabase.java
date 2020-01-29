package com.geovra.red.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geovra.red.item.Item;
import com.geovra.red.item.ItemDao;

@Database(entities = {Item.class}, version = 1)
public abstract class RedDatabase extends RoomDatabase {

  public abstract ItemDao itemDao();

}
