package com.konstantinmuzhik.hw1todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

@Dao
interface ToDoItemDAO {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoItem>>     /* TODO LiveData --> StateFlow ??? */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(toDoData: ToDoItem)

    @Update
    fun updateData(toDoData: ToDoItem)

    @Delete
    fun deleteItem(toDoData: ToDoItem)
}