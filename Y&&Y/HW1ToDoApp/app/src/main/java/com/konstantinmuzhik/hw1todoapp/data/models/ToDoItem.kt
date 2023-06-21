package com.konstantinmuzhik.hw1todoapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoItem(
    @PrimaryKey()
    val id: String,
    val title: String,
    val description: String = "",
    val priority: Priority,
    val deadline: String = "",
    var done: Boolean = false,
    val creationDate: String,
    val changeDate: String = ""
) : Parcelable