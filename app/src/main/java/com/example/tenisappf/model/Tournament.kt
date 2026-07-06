package com.example.tenisappf.model

import com.google.firebase.Timestamp

data class Tournament(
    val id: Int? = null,
    val nombre: String? = null,
    val fecha: Timestamp? = null
)