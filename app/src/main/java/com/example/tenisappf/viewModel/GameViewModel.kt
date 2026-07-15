package com.example.tenisappf.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tenisappf.model.ui.GameUI

class GameViewModel : ViewModel() {
    private var _game = MutableLiveData(GameUI())
    val game: LiveData<GameUI> = _game

    fun updateGame(newGame: GameUI){
        _game.value = newGame
    }

    fun updatePuntajes(puntajeJugador1: Int, puntajeJugador2: Int){
        _game.value!!.puntajeJugador1 = puntajeJugador1
        _game.value!!.puntajeJugador2 = puntajeJugador2
    }
}