package com.example.mysocialmediaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.ActivityRegisterBinding
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val collections = FirebaseFirestore.getInstance().collection("users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {

            val txt1 = binding.edtPassword.text.toString()
            val txt2 = binding.edtPassword2.text.toString()

            if (txt1 != txt2) {
                Toast.makeText(this, "Password shoule be equal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(
                binding.edtLogin.text.toString(),
                binding.edtPassword.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                        val user = User(auth.uid!!, binding.edtLogin.text.toString(), txt1)
                        SharedPreferences.saveUserDetails(user)
                        collections.document(user.uid).set(user)
                        collections.document(user.uid).collection("memes")
                        collections.document(user.uid).collection("followers")
                        collections.document(user.uid).collection("following")
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Unsuccess1", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Unsucces2", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
        }


    }
}