package com.example.tenisappf.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tenisappf.model.ui.GameUI

class GameViewModel : ViewModel() {
    private var _game = MutableLiveData(GameUI())
    val game: LiveData<GameUI> = _game

    fun updateGame(newGame: GameUI) {
        _game.value = newGame
    }

    fun updatePuntajesOrStatus(puntajeJugador1: Int, puntajeJugador2: Int, estado: String) {
        _game.value = GameUI(
            jugador1 = _game.value!!.jugador1,
            jugador2 = _game.value!!.jugador2,
            puntajeJugador1 = puntajeJugador1,
            puntajeJugador2 = puntajeJugador2,
            torneo = _game.value!!.torneo,
            estado = estado
        )
    }
}