package com.konstantinmuzhik.hw1todoapp.fragments

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.fragments.main.MainFragmentDirections


class BindingAdapters {

    companion object {
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun navigateToAddFragment(view: ConstraintLayout, currentItem: ToDoItem) {
            view.setOnClickListener {
                val action = MainFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun navigateToAddFragment(textView: TextView, currentItem: ToDoItem) {
            textView.setOnClickListener {
                val action = MainFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                textView.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(spinner: Spinner, priority: Priority) {
            when (priority) {
                Priority.NO -> spinner.setSelection(0)
                Priority.HIGH -> spinner.setSelection(1)
                Priority.LOW -> spinner.setSelection(2)
            }
        }

        @BindingAdapter("android:hasDeadline")
        @JvmStatic
        fun hasDeadline(switch: SwitchMaterial, deadline: String) {
            switch.isChecked = deadline.isNotEmpty()
        }

        @BindingAdapter("android:parsePriorityBackground")
        @JvmStatic
        fun parsePriorityBackground(checkBox: CheckBox, priority: Priority) {
            when (priority) {
                Priority.HIGH -> checkBox.buttonTintList =
                        ColorStateList.valueOf(checkBox.context.getColor(R.color.red))
                Priority.NO -> checkBox.buttonTintList =
                    ColorStateList.valueOf(checkBox.context.getColor(R.color.black))
                Priority.LOW -> checkBox.buttonTintList =
                    ColorStateList.valueOf(checkBox.context.getColor(R.color.green))
            }
        }
    }
}