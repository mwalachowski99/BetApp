package com.example.betapp.bet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class BetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BetRepository
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    val allBets: MutableLiveData<HashMap<String, Bet>>

    init{
        repository = BetRepository(firebaseDatabase, user)
        allBets = repository.allBets
    }

    suspend fun insert(bet: Bet) = repository.insert(bet)
}