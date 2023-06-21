package com.konstantinmuzhik.hw1todoapp.fragments.add

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentAddBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem as ToDoItem


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.prioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.deadlineSw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chooseDate()
            else binding.deadlineDate.text = ""
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun chooseDate() {
        val calendar = Calendar.getInstance()

        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(calendar)
        }

        val datePicker = DatePickerDialog(
            requireContext(),
            datePickerListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { _, which ->
            if (which == DialogInterface.BUTTON_NEGATIVE) binding.deadlineSw.isChecked = false
        }

        datePicker.show()
    }

    private fun updateLabel(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.deadlineDate.text = sdf.format(calendar.time).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) insertDataToDB()
        return super.onOptionsItemSelected(item)
    } //TODO read about menu provider ???

    private fun parsePriorityString(a: String): Priority {
        return when (a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }
    }

    private fun insertDataToDB() {
        val name = binding.titleEt.text.toString()
        val text = binding.descriptionEt.text.toString()
        val priority = parsePriorityString(binding.prioritySp.selectedItem.toString())
        val deadline = binding.deadlineDate.text.toString()
        val done = false
        val creationDate = "${getString(R.string.creation_date)} ${
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        }"
        val changeDate = "${getString(R.string.last_change_date)} ${
            LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
        }"

        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.insertData(
                ToDoItem(
                    id = LocalDateTime.now().toString(),
                    title = name,
                    description = text,
                    priority = priority,
                    deadline = deadline,
                    done = done,
                    creationDate = creationDate,
                    changeDate = changeDate
                )
            )

            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_added),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}