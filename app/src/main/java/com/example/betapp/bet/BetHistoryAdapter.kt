package com.example.betapp.bet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.databinding.BetHistoryListElementBinding
import com.example.betapp.game.Game
import java.time.format.DateTimeFormatter
import kotlin.math.round

class BetHistoryAdapter : RecyclerView.Adapter<BetHistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: BetHistoryListElementBinding) : RecyclerView.ViewHolder(binding.root)

    private var bets = emptyList<Bet>()
    private var games = emptyList<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BetHistoryListElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = games.first { it.id == bets[position].gameId}

        holder.binding.textViewTeam1.text = game.team1
        holder.binding.textViewTeam2.text = game.team2
        holder.binding.textViewReward.text = (round((bets[position].output - bets[position].input) * 100)/100).toString()
        holder.binding.textViewDateTime.text = game.dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        if(bets[position].output > 0)
            holder.binding.item.setBackgroundColor(Color.GREEN)
        else
            holder.binding.item.setBackgroundColor(Color.RED)

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