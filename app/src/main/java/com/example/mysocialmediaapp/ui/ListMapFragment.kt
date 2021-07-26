package com.example.mysocialmediaapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.models.ClusterMarker
import com.example.mysocialmediaapp.models.UserLocation
import com.example.mysocialmediaapp.util.MyClusterManagerRenderer
import com.example.mysocialmediaapp.util.SharedPreferences
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager


class ListMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var list_view: ListView
    private lateinit var mMapView: MapView
    private val user_list_location = mutableListOf<UserLocation>()
    private var googleMap: GoogleMap? = null
    private lateinit var mMapBoundry: LatLngBounds
    private var clusterManager: ClusterManager<ClusterMarker>? = null
    private var myClusterManagerRendered: MyClusterManagerRenderer? = null
    private var mClustersMarkers = mutableListOf<ClusterMarker>()


    companion object {
        private const val TAG = "ListMapFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_map_fragment, container, false)
        list_view = view.findViewById(R.id.list_view)
        mMapView = view.findViewById(R.id.map_view)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
        mMapView.onResume()
        user_list_location.clear()
        SharedPreferences.getUserLocations {
            user_list_location.addAll(it)
            setUpCurrentUserLocations()
            setUserListOnTopOfMap()
            addMapMarkers()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setUserListOnTopOfMap() {
        val names = mutableListOf<String>()
        for (k in user_list_location) {
            names.add(k.user.login)
        }
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)

        list_view.adapter = arrayAdapter
        list_view.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {

            }
        })

    }

    private fun setUpCurrentUserLocations() {
        val user = SharedPreferences.getStoredUserDetails()!!
        for (k in user_list_location) {
            if (k.user.uid == user.uid) {
                updateCameraView(k)
                break
            }
        }
    }

    private fun updateCameraView(user: UserLocation) {

        val bottomBoundary = user.geoPoint.latitude - .1
        val leftBoundary = user.geoPoint.longitude - .1
        val topBoundary = user.geoPoint.latitude + .1
        val rightBoundary = user.geoPoint.longitude + .1
        mMapBoundry = LatLngBounds(
            LatLng(bottomBoundary, leftBoundary),
            LatLng(topBoundary, rightBoundary)
        )
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundry, 0))
        googleMap!!.addMarker(
            MarkerOptions().position(
                LatLng(
                    user.geoPoint.latitude,
                    user.geoPoint.longitude
                )
            ).title("My Location")
        )


    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onMapReady(map: GoogleMap) {
        map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("Marker"))
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        this.googleMap = map
        return
    }


    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }


    private fun addMapMarkers() {

        if (googleMap != null) {
            if (clusterManager == null) {
                clusterManager = ClusterManager(requireContext(), googleMap)
            }
            if (myClusterManagerRendered == null) {
                myClusterManagerRendered =
                    MyClusterManagerRenderer(requireContext(), googleMap!!, clusterManager!!)
                clusterManager!!.renderer = myClusterManagerRendered
            }

            for (k in user_list_location) {
                try {
                    var snippet = ""
                    if (k.user.uid == SharedPreferences.getStoredUserDetails()!!.uid) {
                        snippet = "This is you"
                    } else {
                        snippet = "Determine route to ${k.user}"
                    }
                    val avatar = R.drawable.ic_launcher_background
                    val clusterMarker = ClusterMarker()
                    clusterMarker.position = LatLng(k.geoPoint.latitude, k.geoPoint.longitude)
                    clusterMarker.setSnippet(snippet)
                    clusterMarker.user = k.user
                    clusterMarker.icon_picture = avatar
                    clusterMarker.setTitle(k.user.login)
                    clusterManager!!.addItem(clusterMarker)
                    mClustersMarkers.add(clusterMarker)
                    Log.i(TAG, "addMapMarkers: new cluster marker is added ")

                } catch (ex: Exception) {
                    Log.e(TAG, "addMapMarkers: $ex")
                }
            }
            clusterManager!!.cluster()
            setUpCurrentUserLocations()
        }

    }


}