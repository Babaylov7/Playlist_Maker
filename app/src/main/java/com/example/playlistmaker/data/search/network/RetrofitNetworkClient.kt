package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

//    private val itunesBaseUrl = "https://itunes.apple.com"

//    private val retrofit = Retrofit.Builder()
//        .baseUrl(itunesBaseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

//    private val itunesService = retrofit.create(ItunesApi::class.java)

    override fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (dto is TrackSearchRequest) {
            try {
                val resp = itunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()

                return body.apply { resultStatus = SearchStatus.RESPONSE_RECEIVED }
            } catch (e: Exception) {

            }
        }
        return Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
    }
}