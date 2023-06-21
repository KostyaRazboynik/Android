package com.konstantinmuzhik.hw1todoapp.adapters

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.ToDoItemLayoutBinding

class ToDoAdapter: RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {

    var toDoItems = emptyList<ToDoItem>()

    class MyViewHolder(private val binding: ToDoItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoItem: ToDoItem) {

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                ToDoItemViewModel(application = Application()).updateData( // TODO remove instance of view model from adapter
                    ToDoItem(
                        id = toDoItem.id,
                        title = toDoItem.title,
                        description = toDoItem.description,
                        priority = toDoItem.priority,
                        deadline = toDoItem.deadline,
                        done = isChecked,
                        creationDate = toDoItem.creationDate,
                        changeDate = toDoItem.changeDate
                    )
                )
            }

            binding.toDoItem = toDoItem
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ToDoItemLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(toDoItems[position])
    }

    override fun getItemCount(): Int {
        return toDoItems.size
    }

    fun setData(mToDoItems: List<ToDoItem>) {
        val toDoDiffUtil = ToDoItemDiffUtil(toDoItems, mToDoItems)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.toDoItems = mToDoItems
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}