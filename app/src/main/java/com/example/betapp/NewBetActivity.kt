package com.example.betapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.betapp.bet.Bet
import com.example.betapp.bet.BetViewModel
import com.example.betapp.bet.Result
import com.example.betapp.databinding.ActivityNewBetBinding
import com.example.betapp.game.Game
import com.example.betapp.userData.UserData
import com.example.betapp.userData.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

class NewBetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewBetBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewBetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var userData = UserData("", 0.0)

        if(auth.currentUser != null){
            val userDataViewModel = UserDataViewModel(application)
            if(auth.currentUser != null)
                userDataViewModel.userData.observe(this, androidx.lifecycle.Observer {
                    it.let{
                        userData = it
                        supportActionBar?.subtitle = "Current points: ${userData.points}"
                    }
                })
        }

        val game = intent.extras?.get("game") as Game

        binding.chipWin.isChecked = true

        binding.textViewTeam1.text = game.team1
        binding.textViewTeam2.text = game.team2
        binding.textViewDateTime.text = game.dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        binding.imageViewTeam1.setImageResource(this.resources.getIdentifier(game.team1.replace(" ", "_").lowercase(), "drawable", this.packageName))
        binding.imageViewTeam2.setImageResource(this.resources.getIdentifier(game.team2.replace(" ", "_").lowercase(), "drawable", this.packageName))

        binding.chipWin.text = game.rateWin.toString()
        binding.chipDraw.text = game.rateDraw.toString()
        binding.chipLoss.text = game.rateLoss.toString()

        binding.buttonBet.setOnClickListener{
            val input = binding.editTextInput.text.toString().toDoubleOrNull() ?: 0.0
            if(input > 0){
                if(input > userData.points){
                    Toast.makeText(this, "You don't have enough points", Toast.LENGTH_LONG).show()
                }
                else {
                    val betViewModel = BetViewModel(application)
                    val userDataViewModel = UserDataViewModel(application)
                    var rate: Double
                    var result: Result
                    if (binding.chipWin.isChecked) {
                        rate = binding.chipWin.text.toString().toDoubleOrNull() ?: 0.0
                        result = Result.WIN
                    } else if (binding.chipDraw.isChecked) {
                        rate = binding.chipDraw.text.toString().toDoubleOrNull() ?: 0.0
                        result = Result.DRAW
                    } else {
                        rate = binding.chipLoss.text.toString().toDoubleOrNull() ?: 0.0
                        result = Result.LOSS
                    }
                    userData.points = userData.points - input

                    CoroutineScope(Dispatchers.IO).launch {

                        userDataViewModel.update(userData)

                        betViewModel.insert(
                            Bet(
                                id = "",
                                gameId = game.id,
                                rate = rate,
                                result = result,
                                input = input,
                                settled = false,
                                output = 0.0
                            )
                        )
                        finish()
                    }
                }
            }
            else {
                Toast.makeText(this, "You have to fill all required fields to bet", Toast.LENGTH_LONG).show()
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logoutMenu){
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        return true
    }

}