package com.konstantinmuzhik.hw1todoapp.fragments.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.adapters.ToDoAdapter
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentMainBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import com.konstantinmuzhik.hw1todoapp.utils.hideKeyboard

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ToDoAdapter
    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())

        listAdapter = ToDoAdapter()

        setUpRecyclerView()
        mToDoViewModel.getAllData.observe(viewLifecycleOwner) { data ->
            mSharedViewModel.isDatabaseEmpty(data)
            listAdapter.setData(data)
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = listAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        swipeToDelete(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = listAdapter.toDoItems[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(itemToDelete)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedItem(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoItem) {
        Snackbar.make(
            view,
            "${getString(R.string.successfully_removed)} '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        ).apply {
            setAction(getString(R.string.undo)) {
                mToDoViewModel.insertData(deletedItem)
            }
            show()
        }
    }
}