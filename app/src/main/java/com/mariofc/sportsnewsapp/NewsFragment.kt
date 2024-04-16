package com.mariofc.sportsnewsapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.adapter.NewsAdapter
import com.mariofc.sportsnewsapp.adapter.SportNewsAdapter
import com.mariofc.sportsnewsapp.data.New
import com.mariofc.sportsnewsapp.data.SportNew
import com.mariofc.sportsnewsapp.databinding.FragmentNewsBinding
import com.mariofc.sportsnewsapp.databinding.FragmentSportsBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var news: MutableList<New>
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.news = this.requireContext().getData(Array<New>::class.java).toMutableList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_news, container, false)
        binding = FragmentNewsBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }

    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.newsAdapter = NewsAdapter(news, this::onItemContextMenuClick)
        binding.newsRecyclerView.adapter = this.newsAdapter
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun onAddBtnClick(view: View) {
        val new=New("", "", "")
        editNew(new) { new1 ->
            this.news.add(new1)
            this.newsAdapter.notifyItemInserted(news.size - 1)
            Toast.makeText(this.context, "Added successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editNew(new: New, actionType: String = "add", onComplete:(New)->Unit){
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_news,
            if(actionType=="add")"Add news" else "Edit news",
            if(actionType=="add")"Add" else "Update"
        ) { dialogView ->
            if(actionType!="add") {
                dialogView.findViewById<EditText>(R.id.imageUrlText).setText(new.imageUrl)
                dialogView.findViewById<EditText>(R.id.titleText).setText(new.title)
                dialogView.findViewById<EditText>(R.id.descriptionText).setText(new.description)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val url = addDialog.findViewById<EditText>(R.id.imageUrlText)!!.text.toString()
            val title = addDialog.findViewById<EditText>(R.id.titleText)!!.text.toString()
            val description = addDialog.findViewById<EditText>(R.id.descriptionText)!!.text.toString()

            if (url.isEmpty() || title.isEmpty() || description.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            }else if(!Patterns.WEB_URL.matcher(url).matches()){
                Snackbar.make(it, "Invalid image url specified.", Snackbar.LENGTH_SHORT).show()
            } else {
                onComplete(New(url, title, description))
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int):Boolean{
        return when(menuId){
            0 -> {
                val new = this.news[position]
                editNew(new, "edit") { newNew ->
                    this.news[position] = newNew
                    this.newsAdapter.notifyItemChanged(position)
                    Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                true
            }

            1 -> {
                this.news.removeAt(position)
                this.newsAdapter.notifyItemRemoved(position)
                Toast.makeText(this.context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                true
            }

            else->{
                false
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.news.toTypedArray(), Array<New>::class.java)

        super.onPause()
    }
}