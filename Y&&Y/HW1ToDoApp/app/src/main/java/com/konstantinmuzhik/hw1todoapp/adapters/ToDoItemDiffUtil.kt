package com.konstantinmuzhik.hw1todoapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem

class ToDoItemDiffUtil(
    private val oldList: List<ToDoItem>,
    private val newList: List<ToDoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ) = oldList[oldItemPosition] == newList[newItemPosition]
}