package com.example.mysocialmediaapp.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class ClusterMarker : ClusterItem {

    private var title: String = ""
    private var position: LatLng = LatLng(0.0, 0.0)
    private var snippet: String = "snippet"
    var icon_picture: Int = 0
    var user: User = User()


    override fun getSnippet(): String? {
        return snippet
    }

    override fun getPosition(): LatLng {
        return position
    }


    override fun getTitle(): String? {
        return title
    }

    fun setPosition(latLng: LatLng) {
        position = latLng
    }

    fun setSnippet(str: String) {
        snippet = str
    }

    fun setTitle(title: String) {
        this.title = title
    }


}