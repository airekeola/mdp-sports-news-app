package com.mariofc.sportsnewsapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.data.New
import com.mariofc.sportsnewsapp.data.Profile
import com.mariofc.sportsnewsapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()
    }

    private fun initialize(){
        binding.registerBtn.setOnClickListener(this::onRegisterBtnClick)
        binding.loginBtn.setOnClickListener(this::onLoginBtnClick)

    }

    private fun onRegisterBtnClick(view: View){
        val name = binding.nameText.text.toString()
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        val url = binding.imageUrlText.text.toString()
        val about = binding.aboutText.text.toString()
        val sports = binding.sportText.text.toString()

        val profiles  = this.getData(Array<Profile>::class.java).toMutableList()

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || url.isEmpty() || about.isEmpty() || sports.isEmpty()){
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.WEB_URL.matcher(url).matches()){
            Toast.makeText(this, "Invalid image url specified.", Toast.LENGTH_SHORT).show()
        }else if(profiles.any { p-> p.email.equals(email) }) {
            Toast.makeText(this, "Email already exist.", Toast.LENGTH_SHORT).show()
        }else{
            val profile = Profile(name, email, password, url, about, sports)

            profiles.add(profile)
            this.saveData(profiles.toTypedArray(), Array<Profile>::class.java)

            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun onLoginBtnClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        resultLauncher.launch(intent)
    }

}