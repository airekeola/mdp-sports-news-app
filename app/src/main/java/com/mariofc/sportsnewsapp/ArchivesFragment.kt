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
import com.mariofc.sportsnewsapp.adapter.ArchivesAdapter
import com.mariofc.sportsnewsapp.data.Archive
import com.mariofc.sportsnewsapp.databinding.FragmentArchivesBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [ArchivesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArchivesFragment : Fragment() {
    private lateinit var binding: FragmentArchivesBinding
    private lateinit var archives: MutableList<Archive>
    private lateinit var archivesAdapter: ArchivesAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.archives = this.requireContext().getData(Array<Archive>::class.java).toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_archives, container, false)

        binding = FragmentArchivesBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }
    
    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.archivesAdapter = ArchivesAdapter(archives, this::onItemContextMenuClick)
        binding.archivesRecyclerView.adapter = this.archivesAdapter
        binding.archivesRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun onAddBtnClick(view: View) {
        val archive = Archive("", "", "")
        editArchive(archive) { archive1 ->
            this.archives.add(archive1)
            this.archivesAdapter.notifyItemInserted(archives.size - 1)
            Toast.makeText(this.context, "Added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editArchive(archive: Archive, actionType: String = "add", onComplete: (Archive) -> Unit) {
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_archives,
            if (actionType == "add") "Add archives" else "Edit archives",
            if (actionType == "add") "Add" else "Update"
        ) { dialogView ->
            if (actionType != "add") {
                dialogView.findViewById<EditText>(R.id.nameText).setText(archive.name)
                dialogView.findViewById<EditText>(R.id.dateText).setText(archive.date)
                dialogView.findViewById<EditText>(R.id.descriptionText).setText(archive.description)
            }

            val dateText: EditText = dialogView.findViewById(R.id.dateText)
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
                onComplete(Archive(name, date, description))
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int): Boolean {
        return when (menuId) {
            0 -> {
                val archive = this.archives[position]
                editArchive(archive, "edit") { newArchive ->
                    this.archives[position] = newArchive
                    this.archivesAdapter.notifyItemChanged(position)
                    Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            1 -> {
                this.archives.removeAt(position)
                this.archivesAdapter.notifyItemRemoved(position)
                Toast.makeText(this.context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.archives.toTypedArray(), Array<Archive>::class.java)

        super.onPause()
    }
}