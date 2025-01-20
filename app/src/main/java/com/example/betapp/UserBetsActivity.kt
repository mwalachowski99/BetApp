package com.example.betapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.betapp.bet.BetAdapter
import com.example.betapp.bet.BetViewModel
import com.example.betapp.databinding.ActivityUserBetsBinding
import com.example.betapp.game.GameViewModel
import com.example.betapp.userData.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth


class UserBetsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBetsBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(auth.currentUser != null){
            val userDataViewModel = UserDataViewModel(application)

            if(auth.currentUser != null)
                userDataViewModel.userData.observe(this, androidx.lifecycle.Observer {
                    it.let{
                        supportActionBar?.subtitle = "Current points: " + it.points.toString()
                    }
                })
        }

        val betViewModel = BetViewModel(application)
        val gameViewModel = GameViewModel(application)

        val adapter = BetAdapter()

        binding.betsRv.layoutManager = LinearLayoutManager(this)
        binding.betsRv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.betsRv.adapter = adapter

        betViewModel.allBets.observe(this, androidx.lifecycle.Observer {
            it.let{
                adapter.setBets(it.values.toList().filter{ x -> !x.settled})
            }
        })
        gameViewModel.allGames.observe(this, androidx.lifecycle.Observer {
            it.let{
                adapter.setGames(it.values.toList())
            }
        })

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