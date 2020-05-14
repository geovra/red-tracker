package com.geovra.red.app.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geovra.red.item.persistence.Item;
import com.geovra.red.item.persistence.ItemDao;

@Database(entities = {Item.class}, version = 1)
public abstract class RedDatabase extends RoomDatabase {

  public abstract ItemDao itemDao();

}
