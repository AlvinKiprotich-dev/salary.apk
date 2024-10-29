package com.example.salarycal // Replace with your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        // Link UI elements to the corresponding view IDs
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // Set click listeners for buttons
        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        // Check for empty fields
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Sign in with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        // Navigate to HomeScreen activity
                        val intent = Intent(this, HomeScreenActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: Close the login activity
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        // Check for empty fields
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
