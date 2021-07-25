package com.example.mysocialmediaapp.models

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class UserLocation(
    val geoPoint: GeoPoint,
    @ServerTimestamp
    val date: Date?,
    val user: User
) {
    constructor() : this(GeoPoint(0.0, 0.0), Date(), User())
}