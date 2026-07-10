package com.example.tenisappf.model.firebase

import com.google.firebase.firestore.DocumentReference

data class Game(
    val jugador1: DocumentReference? = null,
    val jugador2: DocumentReference? = null,
    val puntajeJugador1: Int? = 0,
    val puntajeJugador2: Int? = 0,
    val torneo: DocumentReference? = null,
    val estado: String? = null
)

