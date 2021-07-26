package com.example.mysocialmediaapp.service

import android.Manifest
import android.app.IntentService
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.mysocialmediaapp.models.User
import com.example.mysocialmediaapp.models.UserLocation
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint

class MyIntentService : IntentService("My Intent Service") {

    private lateinit var fusedLocation: FusedLocationProviderClient


    companion object {
        private const val TAG = "MyIntentService"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fusedLocation = LocationServices.getFusedLocationProviderClient(applicationContext)
        return super.onStartCommand(intent,flags,startId)
    }


    override fun onHandleIntent(intent: Intent?) {
        val user=SharedPreferences.getStoredUserDetails()!!
        while (true) {
            Log.i(TAG, "onHandleIntent: running.... ")
            getLastKnownLocation(user)
            Thread.sleep(5000)
        }
    }

    private fun getLastKnownLocation(user:User) {
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
                Log.i(TAG, "getLastKnownLocation: location : ${location.latitude} ${location.longitude}")
            }
        }

    }


}