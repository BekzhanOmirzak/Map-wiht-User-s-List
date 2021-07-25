package com.example.mysocialmediaapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.models.UserLocation
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.firestore.GeoPoint

class SendLocationService : IntentService("Location send Service") {

    private lateinit var fusedLocation: FusedLocationProviderClient

    init {
        instance = this
    }


    companion object {
        var isRunning = false
        private lateinit var instance: SendLocationService

        private const val TAG = "SendLocationService"

        fun stopService() {
            isRunning = false
            instance.stopSelf()
        }
    }

    @SuppressLint("VisibleForTests")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocation = FusedLocationProviderClient(applicationContext)
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onHandleIntent(intent: Intent?) {
        val user = SharedPreferences.getStoredUserDetails()!!
        try {
            isRunning = true
            while (isRunning) {
                getLastKnownLocation(user)
                Thread.sleep(5000)
            }
        } catch (ex: Exception) {
            Thread.currentThread().interrupt()
        }
    }

    private fun getLastKnownLocation(user: User) {
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
                    UserLocation(geoPoint, null, user)
                SharedPreferences.addUserLocationToServer(userLocation)
                Log.i(
                    TAG,
                    "getLastKnownLocation: location : ${location.latitude} ${location.longitude}"
                )
            }
        }

    }


}