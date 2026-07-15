package com.example.tenisappf.model.ui

import com.example.tenisappf.model.firebase.Player
import com.example.tenisappf.model.firebase.Tournament

data class GameUI(
    val jugador1: Player? = null,
    val jugador2: Player? = null,
    var puntajeJugador1: Int? = 0,
    var puntajeJugador2: Int? = 0,
    val torneo: Tournament? = null,
    val estado: String? = null
)