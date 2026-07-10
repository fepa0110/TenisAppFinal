package com.example.tenisappf.model.firebase

import com.google.firebase.Timestamp

data class Tournament(
    val id: Int? = 0,
    val nombre: String? = null,
    val fecha: Timestamp? = null
)