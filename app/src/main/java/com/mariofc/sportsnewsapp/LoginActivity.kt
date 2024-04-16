package com.mariofc.sportsnewsapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.mariofc.sportsnewsapp.ContextExtension.Companion.getData
import com.mariofc.sportsnewsapp.ContextExtension.Companion.saveData
import com.mariofc.sportsnewsapp.data.Profile
import com.mariofc.sportsnewsapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()
    }

    private fun initialize() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    Snackbar.make(binding.root, "Registration successful.", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        binding.registerBtn.setOnClickListener(this::onRegisterBtnClick)
        binding.loginBtn.setOnClickListener(this::onLoginBtnClick)
    }

    private fun onRegisterBtnClick(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun onLoginBtnClick(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        val profiles = this.getData(Array<Profile>::class.java).toMutableList()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
        } else if (!profiles.any { p ->
                p.email.equals(email, true) && p.password.equals(
                    password,
                    true
                )
            }) {
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show()
        } else {
            val profile = profiles.find { p ->
                p.email.equals(email, true) && p.password.equals(
                    password,
                    true
                )
            }!!

            this.saveData(profile, Profile::class.java)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}