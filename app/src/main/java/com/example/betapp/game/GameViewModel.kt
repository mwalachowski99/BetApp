package com.example.betapp.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase

class GameViewModel(application:Application)  : AndroidViewModel(application){
    private val repository: GameRepository
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val allGames: MutableLiveData<HashMap<String, Game>>

    init{
        repository = GameRepository(firebaseDatabase)
        allGames = repository.allGames
    }
}