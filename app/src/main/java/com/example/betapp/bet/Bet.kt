package com.example.betapp.bet

data class Bet(
    var id: String,
    var gameId: String,
    var rate: Double,
    var result: Result,
    var input: Double,
    var settled: Boolean,
    var output: Double
)
