package com.mariofc.sportsnewsapp

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.adapter.EventsAdapter
import com.mariofc.sportsnewsapp.data.Event
import com.mariofc.sportsnewsapp.databinding.FragmentEventsBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [EventsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private lateinit var events: MutableList<Event>
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.events = this.requireContext().getData(Array<Event>::class.java).toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events, container, false)

        binding = FragmentEventsBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }

    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.eventsAdapter = EventsAdapter(events, this::onItemContextMenuClick)
        binding.eventsRecyclerView.adapter = this.eventsAdapter
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun onAddBtnClick(view: View) {
        val event = Event("", "", "")
        editEvent(event) { event1 ->
            this.events.add(event1)
            this.eventsAdapter.notifyItemInserted(events.size - 1)
            Toast.makeText(this.context, "Added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editEvent(event: Event, actionType: String = "add", onComplete: (Event) -> Unit) {
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_events,
            if (actionType == "add") "Add event" else "Edit event",
            if (actionType == "add") "Add" else "Update"
        ) { dialogView ->
            if (actionType != "add") {
                dialogView.findViewById<EditText>(R.id.nameText).setText(event.name)
                dialogView.findViewById<EditText>(R.id.dateText).setText(event.date)
                dialogView.findViewById<EditText>(R.id.descriptionText).setText(event.description)
            }

            val dateText:EditText = dialogView.findViewById(R.id.dateText)
            dateText.setOnClickListener{
                val day: Int
                val month: Int
                val year: Int

                if(dateText.text.isNotEmpty()){
                    val arr = dateText.text.split('/')
                    month = arr[0].toInt() -1
                    day = arr[1].toInt()
                    year = arr[2].toInt()
                }else{
                    val c = Calendar.getInstance()
                    year = c.get(Calendar.YEAR)
                    month = c.get(Calendar.MONTH)
                    day = c.get(Calendar.DAY_OF_MONTH)
                }
                val datePickerDialog = DatePickerDialog(this.requireContext(), { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // Format the selected date and set it as the text of editTextDate
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                    dateText.setText(dateFormat.format(selectedDate.time))
                }, year, month, day)

                datePickerDialog.show()
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = addDialog.findViewById<EditText>(R.id.nameText)!!.text.toString()
            val date = addDialog.findViewById<EditText>(R.id.dateText)!!.text.toString()
            val description =
                addDialog.findViewById<EditText>(R.id.descriptionText)!!.text.toString()

            if (name.isEmpty() || date.isEmpty() || description.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            } else {
                onComplete(Event(name, date, description))
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int): Boolean {
        return when (menuId) {
            0 -> {
                val event = this.events[position]
                editEvent(event, "edit") { newEvent ->
                    this.events[position] = newEvent
                    this.eventsAdapter.notifyItemChanged(position)
                    Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            1 -> {
                this.events.removeAt(position)
                this.eventsAdapter.notifyItemRemoved(position)
                Toast.makeText(this.context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.events.toTypedArray(), Array<Event>::class.java)

        super.onPause()
    }
}