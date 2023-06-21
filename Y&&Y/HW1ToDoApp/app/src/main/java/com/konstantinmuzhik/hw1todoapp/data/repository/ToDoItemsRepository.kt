package com.konstantinmuzhik.hw1todoapp.data.repository

import androidx.lifecycle.LiveData
import com.konstantinmuzhik.hw1todoapp.data.dao.ToDoItemDAO
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

class ToDoItemsRepository(private val toDoItemDao: ToDoItemDAO) {

    val getAllData: LiveData<List<ToDoItem>> = toDoItemDao.getAllData()

    fun insertData(toDoData: ToDoItem) = toDoItemDao.insertData(toDoData)

    fun updateData(toDoData: ToDoItem) = toDoItemDao.updateData(toDoData)

    fun deleteItem(toDoData: ToDoItem) = toDoItemDao.deleteItem(toDoData)
}
