package com.example.ahmeaah_oblig2.ui

import com.example.ahmeaah_oblig2.Alpaca


data class AlpacaUiState(
    val alpacas: List<Alpaca>?,
    val selectedDistrict: Int,
    val votes: MutableList<Int>,
    val percentage: MutableList<Double>
)
