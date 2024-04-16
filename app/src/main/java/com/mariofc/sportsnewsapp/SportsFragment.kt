package com.mariofc.sportsnewsapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.data.SportNew
import com.mariofc.sportsnewsapp.adapter.SportNewsAdapter
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.databinding.FragmentSportsBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SportsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SportsFragment : Fragment() {
    private lateinit var binding: FragmentSportsBinding
    private lateinit var sportNews: MutableList<SportNew>
    private lateinit var sportsAdapter: SportNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.sportNews = this.requireContext().getData(Array<SportNew>::class.java).toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sports, container, false)
        binding = FragmentSportsBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }

    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.sportsAdapter = SportNewsAdapter(sportNews, this::onItemContextMenuClick)
        binding.sportsRecyclerView.adapter = this.sportsAdapter
        binding.sportsRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
    }

    private fun onAddBtnClick(view: View) {
        val sport=SportNew("", "", "")
        editSport(sport) { sportNew ->
            this.sportNews.add(sportNew)
            this.sportsAdapter.notifyItemInserted(sportNews.size - 1)
            Toast.makeText(this.context, "Added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editSport(sport: SportNew, actionType: String = "add", onComplete:(SportNew)->Unit){
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_sports,
            if(actionType=="add")"Add new sports" else "Edit sports",
            if(actionType=="add")"Add" else "Update"
        ) { dialogView ->
            val spinner: Spinner = dialogView.findViewById(R.id.typeSpinner)
            val types = resources.getStringArray(R.array.sport_types);
            val adapter = ArrayAdapter(
                this.requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                types
            )
            spinner.adapter = adapter
            if(actionType=="add") {
                spinner.setSelection(0)
            }else{
                spinner.setSelection(types.indexOf(sport.type))
                dialogView.findViewById<EditText>(R.id.headerText).setText(sport.header)
                dialogView.findViewById<EditText>(R.id.bodyText).setText(sport.body)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val types = resources.getStringArray(R.array.sport_types)
            val type = addDialog.findViewById<Spinner>(R.id.typeSpinner)!!.selectedItem.toString()
            val name = addDialog.findViewById<EditText>(R.id.headerText)!!.text.toString()
            val body = addDialog.findViewById<EditText>(R.id.bodyText)!!.text.toString()
            if (types[0].equals(type) || name.isEmpty() || body.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            } else {
                val sportNew = SportNew(type, name, body)
                onComplete(sportNew)
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int):Boolean{
        return when(menuId){
            0 -> {
                val sport = this.sportNews[position]
                editSport(sport, "edit") { sportNew ->
                    this.sportNews[position] = sportNew
                    this.sportsAdapter.notifyItemChanged(position)
                    Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            1 -> {
                this.sportNews.removeAt(position)
                this.sportsAdapter.notifyItemRemoved(position)
                Toast.makeText(this.context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                true
            }

            else->{
                false
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.sportNews.toTypedArray(), Array<SportNew>::class.java)

        super.onPause()
    }
}