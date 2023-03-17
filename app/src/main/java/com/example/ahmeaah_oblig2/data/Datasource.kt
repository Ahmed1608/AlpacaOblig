package com.example.ahmeaah_oblig2.data

import com.example.ahmeaah_oblig2.AlpacaParty
import com.example.ahmeaah_oblig2.Votes
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class Datasource {
    val client = OkHttpClient()

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val retro: Retrofit = Retrofit.Builder()
        .baseUrl("https://in2000-proxy.ifi.uio.no")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    interface GetData {
        @GET("/alpacaapi/alpacaparties")
        suspend fun getAlpacas(): AlpacaParty

        @GET("/alpacaapi/district1")
        suspend fun getVotesDistrict1(): List<Votes>

        @GET("/alpacaapi/district2")
        suspend fun getVotesDistrict2(): List<Votes>

    }


}


