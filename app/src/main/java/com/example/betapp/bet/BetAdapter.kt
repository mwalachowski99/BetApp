package com.example.betapp.bet

import com.example.betapp.game.Game
import com.example.betapp.game.GameViewModel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.NewBetActivity
import com.example.betapp.databinding.BetListElementBinding
import com.example.betapp.databinding.GameListElementBinding
import java.time.format.DateTimeFormatter

class BetAdapter : RecyclerView.Adapter<BetAdapter.ViewHolder>() {
    class ViewHolder(val binding: BetListElementBinding) : RecyclerView.ViewHolder(binding.root)

    private var bets = emptyList<Bet>()
    private var games = emptyList<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BetListElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games.first { it.id == bets[position].gameId}

        holder.binding.textViewTeam1.text = game.team1
        holder.binding.textViewTeam2.text = game.team2
        holder.binding.textViewRate.text = bets[position].rate.toString()
        holder.binding.textViewInput.text = bets[position].input.toString()
        holder.binding.textViewResult.text = bets[position].result.toString()
        holder.binding.textViewDateTime.text = game.dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        val context = holder.binding.imageViewTeam1.context
        holder.binding.imageViewTeam1.setImageResource(context.resources.getIdentifier(game.team1.replace(" ", "_").lowercase(), "drawable", context.packageName))
        holder.binding.imageViewTeam2.setImageResource(context.resources.getIdentifier(game.team2.replace(" ", "_").lowercase(), "drawable", context.packageName))

    }

    override fun getItemCount(): Int = bets.size

    fun setBets(allBets: List<Bet>){
        bets = allBets
        notifyDataSetChanged()
    }

    fun setGames(allGames: List<Game>){
        games = allGames
        notifyDataSetChanged()
    }
}