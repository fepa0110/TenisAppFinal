package com.example.tenisappf.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun getTournaments(tenisDatabase: FirebaseFirestore) {
    tenisDatabase.collection("tournaments").get().addOnSuccessListener { result ->
        for (document in result) {
            Log.d("DATABASE", "${document.id} => ${document.data}")
        }
    }.addOnFailureListener { exception ->
        Log.w("DATABASE", "Error getting documents.", exception)
    }
}