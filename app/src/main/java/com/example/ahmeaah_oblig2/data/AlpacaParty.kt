package com.example.ahmeaah_oblig2

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AlpacaParty(
    val parties: List<Alpaca>
)



@JsonClass(generateAdapter = true)
data class Alpaca(

    val id: String,
    val name: String,
    val leader: String,
    val img: String,
    val color: String

)

data class Votes(
    val id: String,
    val percent: Int?
)

data class Party(

    val id: String,
    val votes: String
)



