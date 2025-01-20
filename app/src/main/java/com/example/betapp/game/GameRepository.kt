package com.example.betapp.game

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.snapshots
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GameRepository(private val fbdb: FirebaseDatabase) {
    val allGames: MutableLiveData<HashMap<String, Game>> =
        MutableLiveData<HashMap<String, Game>>().also{
            it.value = HashMap<String, Game>()
        }

    init {
        fbdb.getReference("games").addChildEventListener(
            object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val game = Game(
                        id = snapshot.ref.key as String,
                        team1 = snapshot.child("team1").getValue(String::class.java)!!,
                        team2 = snapshot.child("team2").getValue(String::class.java)!!,
                        rateWin = snapshot.child("rateWin").getValue(Double::class.java)!!,
                        rateDraw = snapshot.child("rateDraw").getValue(Double::class.java)!!,
                        rateLoss = snapshot.child("rateLoss").getValue(Double::class.java)!!,
                        round = snapshot.child("round").getValue(String::class.java)!!,
                        dateTime = LocalDateTime.parse(
                            snapshot.child("dateTime").getValue(String::class.java), pattern
                        )!!
                    )
                    allGames.value?.put(game.id, game)
                    allGames.postValue(allGames.value)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val game = Game(
                        id = snapshot.ref.key as String,
                        team1 = snapshot.child("team1").getValue(String::class.java)!!,
                        team2 = snapshot.child("team2").getValue(String::class.java)!!,
                        rateWin = snapshot.child("rateWin").getValue(Double::class.java)!!,
                        rateDraw = snapshot.child("rateDraw").getValue(Double::class.java)!!,
                        rateLoss = snapshot.child("rateLoss").getValue(Double::class.java)!!,
                        round = snapshot.child("round").getValue(String::class.java)!!,
                        dateTime = snapshot.child("dateTime").getValue(LocalDateTime::class.java)!!
                    )
                    allGames.value?.put(game.id, game)
                    allGames.postValue(allGames.value)
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    allGames.value?.remove(snapshot.ref.key)
                    allGames.postValue(allGames.value)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }

        )
    }
}
