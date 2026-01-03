package com.example.myapplication.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null,
    val color: Int = 0
) {
    // No-argument constructor for Firestore
    constructor() : this("", "", "", "", null, null, 0)
}
