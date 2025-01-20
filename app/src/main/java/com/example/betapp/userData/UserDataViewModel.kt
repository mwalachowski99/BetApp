package com.example.betapp.userData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserDataViewModel(application: Application)  : AndroidViewModel(application){
    private val repository: UserDataRepository
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    var userData: MutableLiveData<UserData>

    init{
        repository = UserDataRepository(firebaseDatabase, user)
        userData = repository.userData
    }

    suspend fun insert(userData: UserData) = repository.insert(userData)

    suspend fun update(userData: UserData) = repository.update(userData)


}