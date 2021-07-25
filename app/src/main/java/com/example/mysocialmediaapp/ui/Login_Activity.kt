package com.example.mysocialmediaapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysocialmediaapp.databinding.ActivityLoginBinding
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val collections = FirebaseFirestore.getInstance().collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()

        SharedPreferences.init(this)
        val user = SharedPreferences.getStoredUserDetails()
        if (user != null) {
            auth.signInWithEmailAndPassword(user.login, user.password)
            collections.document(user.uid).set(user)
            collections.document(user.uid).collection("memes")
            collections.document(user.uid).collection("followers")
            collections.document(user.uid).collection("following")
            directUserToMainActivity()
        }

        binding.btnLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.edtLogin.text.toString(),
                binding.edtPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val user =
                        User(
                            auth.uid!!,
                            binding.edtLogin.text.toString(),
                            binding.edtPassword.text.toString()
                        )
                    SharedPreferences.saveUserDetails(user)
                    collections.document(user.uid).set(user)
                    collections.document(user.uid).collection("memes")
                    collections.document(user.uid).collection("followers")
                    collections.document(user.uid).collection("following")
                    directUserToMainActivity()
                } else {
                    Toast.makeText(this, "Please,try smt went wrong", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
            }
        }



        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun directUserToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}