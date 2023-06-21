package com.konstantinmuzhik.hw1todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.konstantinmuzhik.hw1todoapp.data.ToDoItemDatabase
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.repository.ToDoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoItemViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = ToDoItemDatabase.getDatabase(application).toDoItemDao()
    private val repository: ToDoItemsRepository = ToDoItemsRepository(todoDao)
    val getAllData: LiveData<List<ToDoItem>> = repository.getAllData
    
    fun insertData(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }
}