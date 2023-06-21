package com.konstantinmuzhik.hw1todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.konstantinmuzhik.hw1todoapp.data.dao.ToDoItemDAO
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

@Database(entities = [ToDoItem::class], version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun toDoItemDao(): ToDoItemDAO

    companion object {
        @Volatile
        private var INSTANCE: ToDoItemDatabase? = null
        fun getDatabase(context: Context): ToDoItemDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoItemDatabase::class.java,
                        "todo_database"
                    ).build()
                }

                return INSTANCE as ToDoItemDatabase
            }
        }
    }
}