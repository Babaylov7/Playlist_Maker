package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateNetworkRequest() {

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)
    private val tracks = ArrayList<Track>()

    fun sendRequest(requestValue: String): NetworkQueryResult {
        var searchStatus = SearchStatus.DEFAULT
        tracks.clear()
        itunesService.search(requestValue)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(                                //Ответ
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            searchStatus = SearchStatus.RESPONSE_RECEIVED
                        }
                        if (tracks.isEmpty()) {
                            searchStatus = SearchStatus.LIST_IS_EMPTY
                        }
                    }
                }
                override fun onFailure(
                    call: Call<TrackResponse>,
                    t: Throwable
                ) {                                                 //Возврат ошибки
                    searchStatus = SearchStatus.NETWORK_ERROR
                }
            })

        return NetworkQueryResult(searchStatus, tracks)
    }


}