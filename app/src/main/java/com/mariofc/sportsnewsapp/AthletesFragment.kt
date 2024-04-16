package com.mariofc.sportsnewsapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.adapter.AthletesAdapter
import com.mariofc.sportsnewsapp.data.Athlete
import com.mariofc.sportsnewsapp.data.SportNew
import com.mariofc.sportsnewsapp.databinding.FragmentAthletesBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AthletesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AthletesFragment : Fragment() {
    private lateinit var binding: FragmentAthletesBinding
    private lateinit var athletes: MutableList<Athlete>
    private lateinit var athletesAdapter: AthletesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.athletes = this.requireContext().getData(Array<Athlete>::class.java).toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_athletes, container, false)

        binding = FragmentAthletesBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }

    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.athletesAdapter = AthletesAdapter(athletes, this::onItemContextMenuClick)
        binding.athletesRecyclerView.adapter = this.athletesAdapter
        binding.athletesRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun onAddBtnClick(view: View) {
        val athlete = Athlete("", "", "", "", "", "")
        editAthlete(athlete) { newAthlete ->
            this.athletes.add(newAthlete)
            this.athletesAdapter.notifyItemInserted(athletes.size - 1)
            Toast.makeText(this.context, "Added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editAthlete(athlete: Athlete, actionType: String = "add", onComplete:(Athlete)->Unit){
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_athletes,
            if(actionType=="add")"Add new athlete" else "Edit athlete",
            if(actionType=="add")"Add" else "Update"
        ) { dialogView ->
            if(actionType!="add") {
                dialogView.findViewById<EditText>(R.id.athleteNameText).setText(athlete.name)
                dialogView.findViewById<EditText>(R.id.sportNameText).setText(athlete.sport)
                dialogView.findViewById<EditText>(R.id.countryNameText).setText(athlete.country)
                dialogView.findViewById<EditText>(R.id.performanceText).setText(athlete.performance)
                dialogView.findViewById<EditText>(R.id.medalsText).setText(athlete.medals)
                dialogView.findViewById<EditText>(R.id.factsText).setText(athlete.facts)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = addDialog.findViewById<EditText>(R.id.athleteNameText)!!.text.toString()
            val sport = addDialog.findViewById<EditText>(R.id.sportNameText)!!.text.toString()
            val country = addDialog.findViewById<EditText>(R.id.countryNameText)!!.text.toString()
            val performance = addDialog.findViewById<EditText>(R.id.performanceText)!!.text.toString()
            val medals = addDialog.findViewById<EditText>(R.id.medalsText)!!.text.toString()
            val facts = addDialog.findViewById<EditText>(R.id.factsText)!!.text.toString()

            if (name.isEmpty() || sport.isEmpty() || country.isEmpty() || performance.isEmpty() || medals.isEmpty() || facts.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            } else {
                val newAthlete = Athlete(name, sport, country, performance, medals, facts)
                onComplete(newAthlete)
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int):Boolean{
        return when(menuId){
            0 -> {
                val athlete = this.athletes[position]
                editAthlete(athlete, "edit") { newAthlete ->
                    this.athletes[position] = newAthlete
                    this.athletesAdapter.notifyItemChanged(position)
                    Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            1 -> {
                this.athletes.removeAt(position)
                this.athletesAdapter.notifyItemRemoved(position)
                Toast.makeText(this.context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                true
            }

            else->{
                false
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.athletes.toTypedArray(), Array<Athlete>::class.java)

        super.onPause()
    }
}