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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameUI

        if (puntajeJugador1 != other.puntajeJugador1) return false
        if (puntajeJugador2 != other.puntajeJugador2) return false
        if (jugador1 != other.jugador1) return false
        if (jugador2 != other.jugador2) return false
        if (torneo != other.torneo) return false
        if (estado != other.estado) return false

        return true
    }
}