package com.konstantinmuzhik.hw1todoapp.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.konstantinmuzhik.hw1todoapp.R
import com.konstantinmuzhik.hw1todoapp.data.models.Priority
import com.konstantinmuzhik.hw1todoapp.data.models.ToDoItem
import com.konstantinmuzhik.hw1todoapp.data.viewmodel.ToDoItemViewModel
import com.konstantinmuzhik.hw1todoapp.databinding.FragmentUpdateBinding
import com.konstantinmuzhik.hw1todoapp.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoItemViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.setArgs(args) /* TODO use ViewBinding instead of DataBinding */

        binding.currentPrioritySp.onItemSelectedListener = mSharedViewModel.spinnerListener

        binding.currentDeadlineSw.setOnClickListener {
            if (binding.currentDeadlineDate.text.toString().isEmpty()) {
                binding.currentDeadlineSw.isChecked = true
                chooseDate()
            }
            else {
                binding.currentDeadlineDate.text = ""
                binding.currentDeadlineSw.isChecked = false
            }
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
            if (which == DialogInterface.BUTTON_NEGATIVE)binding.currentDeadlineSw.isChecked = false
        }

        datePicker.show()
    }

    private fun updateLabel(calendar: Calendar) {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.currentDeadlineDate.text = sdf.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setPositiveButton(getString(R.string.yes_pos_btn)) { _, _ ->
                mToDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.successfully_removed)}: '${args.currentItem.title}'",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton(getString(R.string.no_neg_btn)) { _, _ -> }
            setTitle("${getString(R.string.delete_warning)} '${args.currentItem.title}'")
            setMessage("${getString(R.string.warning)} '${args.currentItem.title}'?")
            create()
        }

        alertDialog.show()
    }

    private fun parsePriorityString(a: String): Priority {
        return when(a) {
            getString(R.string.high_priority) -> Priority.HIGH
            getString(R.string.low_priority) -> Priority.LOW
            else -> Priority.NO
        }
    }

    private fun updateItem() {
        val name = binding.currentTitleEt.text.toString()
        val text = binding.currentDescriptionEt.text.toString()
        val priority = parsePriorityString(binding.currentPrioritySp.selectedItem.toString())
        val deadline = binding.currentDeadlineDate.text.toString()
        val done = args.currentItem.done
        val creationDate = args.currentItem.creationDate
        val changeDate =
            "${getString(R.string.last_change_date)} ${
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
            }"
        if (mSharedViewModel.verifyDataFromUser(name)) {
            mToDoViewModel.updateData(
                ToDoItem(
                    id = args.currentItem.id,
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
                getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_title),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}