package com.example.betapp.game

import java.time.LocalDateTime

data class Game(
    var id: String,
    var team1: String,
    var team2: String,
    var rateWin: Double,
    var rateDraw: Double,
    var rateLoss: Double,
    var round: String,
    var dateTime: LocalDateTime

) : java.io.Serializable
