package com.example.tenisappf.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tenisappf.model.firebase.Tournament

class TournamentsListViewModel() : ViewModel() {
    var _tournamentsList = MutableLiveData<List<Tournament>>()
    val tournamentsList: LiveData<List<Tournament>> = _tournamentsList

}