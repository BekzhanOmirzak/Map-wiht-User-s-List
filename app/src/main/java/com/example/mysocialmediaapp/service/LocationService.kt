package com.example.mysocialmediaapp.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.mysocialmediaapp.models.UserLocation
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.gms.location.*
import com.google.firebase.firestore.GeoPoint


class LocationService : Service() {

    private lateinit var fusedLocation: FusedLocationProviderClient

    companion object {
        val update_interval = 4 * 1000
        val fastest_interval = 2000
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocation = LocationServices.getFusedLocationProviderClient(applicationContext)

        if (Build.VERSION.SDK_INT >= 26) {
            val channel_id = "my_channel 1"
            val channel = NotificationChannel(
                channel_id,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification = NotificationCompat.Builder(this, channel_id)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)

        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getLastKnownLocation()
        return START_NOT_STICKY
    }

    private fun getLastKnownLocation() {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(update_interval.toLong())
        locationRequest.setFastestInterval(fastest_interval.toLong())

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
            return
        }

        val user = SharedPreferences.getStoredUserDetails()!!
        fusedLocation.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(it: LocationResult) {
                val location = it.lastLocation
                val geoPoint = GeoPoint(location.latitude, location.longitude)
                val userLocation =
                    UserLocation(geoPoint, null, user)
                SharedPreferences.addUserLocationToServer(userLocation)
            }
        }, Looper.myLooper())
    }


}