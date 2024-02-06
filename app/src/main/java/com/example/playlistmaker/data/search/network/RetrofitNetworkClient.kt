package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val itunesService: ItunesApi) : NetworkClient {

    override suspend fun doTrackSearchRequest(dto: TrackSearchRequest): Response {
        if (dto is TrackSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val resp = itunesService.search(dto.expression)
                    resp.apply { resultStatus = SearchStatus.RESPONSE_RECEIVED }
                } catch (e: Exception) {
                    Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
                }
            }
        }
        return Response().apply { resultStatus = SearchStatus.NETWORK_ERROR }
    }
}
