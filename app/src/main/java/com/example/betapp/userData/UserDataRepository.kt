package com.example.betapp.userData
import androidx.lifecycle.MutableLiveData
import com.example.betapp.game.Game
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class UserDataRepository(private val fbdb: FirebaseDatabase, private val user: FirebaseUser) {

    var userData: MutableLiveData<UserData> = MutableLiveData<UserData>()

    init{
        fbdb.getReference("userData/" + user.uid).addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val data = UserData(
                        id = snapshot.ref.key as String,
                        points = snapshot.child("points").getValue(Double::class.java)!!,
                    )
                    userData.value = data
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val data = UserData(
                        id = snapshot.ref.key as String,
                        points = snapshot.child("points").getValue(Double::class.java)!!,
                    )
                    userData.value = data
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )
    }

    fun insert(userData: UserData){
        fbdb.getReference("userData/" + user.uid).push().also{
            userData.id = it.ref.key.toString()
            it.setValue(userData)
        }
    }

    fun update(userData: UserData){
        var userDataRef = fbdb.getReference("userData/${user.uid}/${userData.id}")
        userDataRef.child("points").setValue(userData.points)
    }

}