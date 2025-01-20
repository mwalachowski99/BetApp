package com.example.betapp.game


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.betapp.NewBetActivity
import com.example.betapp.databinding.GameListElementBinding
import java.time.format.DateTimeFormatter

class GameAdapter : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    class ViewHolder(val binding: GameListElementBinding) : RecyclerView.ViewHolder(binding.root)

    private var games = emptyList<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = GameListElementBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.textViewTeam1.text = games[position].team1
        holder.binding.textViewTeam2.text = games[position].team2
        holder.binding.textViewRateWin.text = games[position].rateWin.toString()
        holder.binding.textViewRateDraw.text = games[position].rateDraw.toString()
        holder.binding.textViewRateLoss.text = games[position].rateLoss.toString()
        holder.binding.textViewDateTime.text = games[position].dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        val context = holder.binding.imageViewTeam1.context
        holder.binding.imageViewTeam1.setImageResource(context.resources.getIdentifier(games[position].team1.replace(" ", "_").lowercase(), "drawable", context.packageName))
        holder.binding.imageViewTeam2.setImageResource(context.resources.getIdentifier(games[position].team2.replace(" ", "_").lowercase(), "drawable", context.packageName))



            holder.binding.root.setOnLongClickListener {
                val intent = Intent(holder.binding.root.context, NewBetActivity::class.java)
                intent.putExtra("game", games[position])
                ContextCompat.startActivity(
                    holder.binding.root.context,
                    intent,
                    Bundle()
                )
                true
            }
        }

    override fun getItemCount(): Int = games.size

    fun setGames(allGames: List<Game>){
        games = allGames
        notifyDataSetChanged()
    }
}