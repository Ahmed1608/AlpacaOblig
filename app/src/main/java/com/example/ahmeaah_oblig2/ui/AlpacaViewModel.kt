package com.example.ahmeaah_oblig2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahmeaah_oblig2.*
import com.example.ahmeaah_oblig2.data.Datasource
import com.example.ahmeaah_oblig2.data.XMLParser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Request
import java.io.InputStream
import java.net.URL

class AlpacaViewModel : ViewModel() {

    private val uiState = MutableStateFlow(AlpacaUiState(listOf(), 1, mutableListOf(),  mutableListOf()))
    val stateFlow: StateFlow<AlpacaUiState> = uiState.asStateFlow()
    var districtVotes = mutableListOf(0, 0, 0, 0)
    var alpaca: List<Alpaca>? = listOf()
    var percentage = mutableListOf(0.0,0.0,0.0,0.0)

    init {

        createAlpacas()
        createDistrictVotes()

    }

    fun createAlpacas() {

        viewModelScope.launch(Dispatchers.IO) {

            val alpacagang: List<Alpaca>?
            val datasource = Datasource()
            val getData = datasource.retro.create(Datasource.GetData::class.java)


            val alpacas: AlpacaParty = getData.getAlpacas()
            alpacagang = alpacas.parties
            alpaca = alpacas.parties
            uiState.value = AlpacaUiState(alpacagang, 1 ,districtVotes,percentage)



        }
    }
    fun updateDistrict(district: Int) {
        uiState.value = AlpacaUiState(alpaca,district,districtVotes,percentage)
        createDistrictVotes()

    }

    fun createDistrictVotes(){

        val selectedIndex = uiState.value.selectedDistrict




        viewModelScope.launch(Dispatchers.IO) {




        if (selectedIndex == 1) {



                val datasource = Datasource()
                val getData = datasource.retro.create(Datasource.GetData::class.java)



                districtVotes = countVotes(getData.getVotesDistrict1())
                findPercentage2(percentage,districtVotes)




        } else if (selectedIndex == 2) {
            //var votes: List<Votes>?
            val datasource = Datasource()
            val getData = datasource.retro.create(Datasource.GetData::class.java)
            districtVotes = countVotes(getData.getVotesDistrict2())

            findPercentage2(percentage,districtVotes)




        } else if (selectedIndex == 3) {
            var listOfVotes: List<Party>? = null
            val request = Request.Builder()
                .url(URL("https://in2000-proxy.ifi.uio.no/alpacaapi/district3"))
                .build()
            val datasource = Datasource()
            val response = datasource.client.newCall(request).execute().body?.string()

            if (response != null) {

                val inputStream: InputStream = response.byteInputStream()
                listOfVotes = XMLParser().parse(inputStream)


            }


            districtVotes = countVotes2(listOfVotes)
            findPercentage2(percentage,districtVotes)





        }
            uiState.value = AlpacaUiState(alpacas = alpaca, selectedIndex,districtVotes,percentage)
    }

    }


    fun findPercentage(votes: Int, sum: Int): String {
        val value = ((votes.toDouble() / sum) * 100)
        return String.format("%.1f", value)

    }

    fun findPercentage2(percentage:MutableList<Double>,districtVotes:MutableList<Int>){
        var i = 0

        for(vote in districtVotes){
            percentage[i] = 0.0
            percentage[i] =findPercentage(vote, districtVotes.sum()).toDouble()
            i++
            if(i>3){
                break
            }
        }
    }


    fun countVotes(votes:List<Votes>):MutableList<Int>{
        val districtVotes = mutableListOf(0,0,0,0)
        for (vote in votes) {

            val indeks = vote.id.toInt()
            districtVotes[indeks - 1]++

        }

        return  districtVotes
    }

    fun countVotes2( listOfVotes: List<Party>?): MutableList<Int>{
        val districtVotes = mutableListOf(0,0,0,0)
        if (listOfVotes != null) {
            for (party in listOfVotes) {
                val indeks = party.id.toInt()
                districtVotes[indeks - 1] = party.votes.toInt()
            }
        }
        return districtVotes
    }



}