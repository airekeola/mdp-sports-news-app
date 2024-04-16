package com.mariofc.sportsnewsapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.data.Profile
import com.mariofc.sportsnewsapp.databinding.FragmentAboutBinding
import com.mariofc.sportsnewsapp.dialog.AppDialogBuilder

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding
    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        this.profile = this.requireContext().getData(Profile::class.java, "{}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        binding = FragmentAboutBinding.bind(view)

        // init fragment
        initialize()

        return binding.root
    }

    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onEditBtnClick)
        binding.logoutBtn.setOnClickListener(this::onlogoutBtnClick)

        if (this.profile.imageUrl.isNullOrEmpty()) {
            this.binding.profileImage.setImageResource(R.drawable.avatar)
        } else {
            Glide.with(this).load(profile.imageUrl).into(this.binding.profileImage)
        }
        this.binding.nameTextView.text = profile.name ?: "---"
        this.binding.aboutTextView.text = profile.about ?: "---"
        this.binding.sportTextView.text = profile.sports ?: "---"
    }

    private fun onEditBtnClick(view: View) {
        editProfile(this.profile, "edit") { profile ->
            this.profile = profile
            this.initialize()
            Toast.makeText(this.context, "Updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onlogoutBtnClick(view: View){
        val intent = Intent(this.context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun editProfile(profile: Profile, actionType: String = "add", onComplete: (Profile) -> Unit) {
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_profile,
            if (actionType == "add") "Add profile" else "Edit profile",
            if (actionType == "add") "Add" else "Update"
        ) { dialogView ->
            if (actionType != "add") {
                dialogView.findViewById<EditText>(R.id.nameText).setText(profile.name)
                dialogView.findViewById<EditText>(R.id.imageUrlText).setText(profile.imageUrl)
                dialogView.findViewById<EditText>(R.id.aboutText).setText(profile.about)
                dialogView.findViewById<EditText>(R.id.sportText).setText(profile.sports)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = addDialog.findViewById<EditText>(R.id.nameText)!!.text.toString()
            val url = addDialog.findViewById<EditText>(R.id.imageUrlText)!!.text.toString()
            val about = addDialog.findViewById<EditText>(R.id.aboutText)!!.text.toString()
            val sports = addDialog.findViewById<EditText>(R.id.sportText)!!.text.toString()

            if (name.isEmpty() || url.isEmpty() || about.isEmpty() || sports.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            }else if(!Patterns.WEB_URL.matcher(url).matches()){
                Snackbar.make(it, "Invalid image url specified.", Snackbar.LENGTH_SHORT).show()
            } else {
                onComplete(Profile(name, profile.email, profile.password, url, about, sports))
                addDialog.dismiss()
            }
        }
    }

    override fun onPause() {
        this.requireContext().saveData(this.profile, Profile::class.java)

        super.onPause()
    }
}