package com.example.betapp.bet

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class BetRepository(private val fbdb: FirebaseDatabase, private val user: FirebaseUser) {

    val allBets: MutableLiveData<HashMap<String, Bet>> =
        MutableLiveData<HashMap<String, Bet>>().also{
            it.value = HashMap<String, Bet>()
        }

    init{
        fbdb.getReference("bets/" + user.uid).addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val bet = Bet(
                        id = snapshot.ref.key as String,
                        gameId = snapshot.child("gameId").getValue(String::class.java)!!,
                        rate = snapshot.child("rate").getValue(Double::class.java)!!,
                        result = snapshot.child("result").getValue(Result::class.java)!!,
                        input = snapshot.child("input").getValue(Double::class.java)!!,
                        settled = snapshot.child("settled").getValue(Boolean::class.java)!!,
                        output = snapshot.child("output").getValue(Double::class.java)!!,
                    )
                    allBets.value?.put(bet.id, bet)
                    allBets.postValue(allBets.value)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val bet = Bet(
                        id = snapshot.ref.key as String,
                        gameId = snapshot.child("gameId").getValue(String::class.java)!!,
                        rate = snapshot.child("rate").getValue(Double::class.java)!!,
                        result = snapshot.child("result").getValue(Result::class.java)!!,
                        input = snapshot.child("input").getValue(Double::class.java)!!,
                        settled = snapshot.child("settled").getValue(Boolean::class.java)!!,
                        output = snapshot.child("output").getValue(Double::class.java)!!,
                    )
                    allBets.value?.set(bet.id, bet)
                    allBets.postValue(allBets.value)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    allBets.value?.remove(snapshot.ref.key)
                    allBets.postValue(allBets.value)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )
    }

    fun insert(bet: Bet){
        fbdb.getReference("bets/" + user.uid).push().also{
            bet.id = it.ref.key.toString()
            it.setValue(bet)
        }
    }

}