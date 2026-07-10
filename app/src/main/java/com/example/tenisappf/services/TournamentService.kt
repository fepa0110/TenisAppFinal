package com.example.tenisappf.services

import android.util.Log
import com.example.tenisappf.model.firebase.Tournament
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

private const val TAG = "TournamentsService"

class TournamentService() {
    companion object {
        fun getTournaments(tenisDatabase: FirebaseFirestore): MutableMap<String, Tournament> {

            val tournaments = mutableMapOf<String, Tournament>()

            tenisDatabase.collection("tournaments")
                .get()
                .addOnSuccessListener { result ->
                    // Clear existing data to avoid duplicates on recomposition
                    //            tournaments.clear()
                    result.forEach { document ->
                        val tournament = document.toObject<Tournament>()
                        tournaments[document.id] = Tournament(
                            id = tournament.id,
                            nombre = tournament.nombre,
                            fecha = tournament.fecha
                        )
                    }

                    return@addOnSuccessListener
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

            return tournaments
        }
    }

}
