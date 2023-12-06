package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest

class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

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