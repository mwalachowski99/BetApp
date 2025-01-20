package com.example.betapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.betapp.databinding.ActivityRegisterBinding
import com.example.betapp.userData.UserData
import com.example.betapp.userData.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.buttonLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.buttonRegister.setOnClickListener{
            try{
                var email = binding.editTextEmail.text.toString()
                var password = binding.editTextPassword.text.toString()
                var confirmPassword = binding.editTextConfirmPassword.text.toString()

                if(password != confirmPassword){
                    throw Exception("Password is not match")
                }

                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show()
                        auth.signInWithEmailAndPassword(
                            email,
                            password,
                        )
                        val userDataViewModel = UserDataViewModel(application)

                        CoroutineScope(Dispatchers.IO).launch {
                            userDataViewModel.insert(
                                UserData(
                                    id = "",
                                    points = 50.0,
                                )
                            )
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                        this.finish()
                    }
                    else{
                        Toast.makeText(this, "Problem with registration: " + it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }catch (e: Exception){
                Toast.makeText(
                this,
                 e?.message,
                Toast.LENGTH_LONG
            ).show()
        }
        }

    }
    override fun onResume() {
        super.onResume()

        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}