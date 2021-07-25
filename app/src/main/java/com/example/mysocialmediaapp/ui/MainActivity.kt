package com.example.mysocialmediaapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.adapter.MemeRecyclerAdapter
import com.example.mysocialmediaapp.models.ChatMessage
import com.example.mysocialmediaapp.models.Meme
import com.example.mysocialmediaapp.models.UserLocation
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MainActivity : AppCompatActivity(), MemeRecyclerAdapter.AddMeme,
    FragmentChatMessage.onSendMessage {

    lateinit var bottom_nav_view: BottomNavigationView
    lateinit var nav_controller: NavController
    private val TAG = "MainActivity"
    private lateinit var fusedLocation: FusedLocationProviderClient


    private val collections =
        FirebaseFirestore.getInstance().collection("users")
    val memes_col = collections.document(FirebaseAuth.getInstance().uid!!).collection("memes")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_nav_view = findViewById(R.id.bottom_nav_view)
        nav_controller =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        bottom_nav_view.setupWithNavController(nav_controller)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocation.lastLocation.addOnCompleteListener {
            if (it.isSuccessful) {
                val location = it.result
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                val userLocation =
                    UserLocation(geoPoint, null, SharedPreferences.getStoredUserDetails()!!)
                SharedPreferences.addUserLocationToServer(userLocation)
                Log.i(TAG, "getLastKnownLocation: location : ${location.latitude} ${location.longitude}")
            }
        }
    }


    override fun onAddMeme(post: Meme) {
        var exist = false

        memes_col.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (k in it.result!!) {
                    val meme = k.toObject(Meme::class.java)
                    if (meme.url == post.url) {
                        exist = true
                        Toast.makeText(
                            this,
                            "It is already in our profile",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnCompleteListener
                    }
                }
            }
        }
        if (!exist) {
            memes_col.add(post)
            Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSendMessage(chat_id: String, message: ChatMessage) {
        Log.i(TAG, "onSendMessage: Interface is working ")
        SharedPreferences.sendMessage(chat_id, message)
        getLastKnownLocation()
    }

}