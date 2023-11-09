package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.SearchStatus
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.models.TrackSearchResult
import com.example.playlistmaker.domain.api.track.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): TrackSearchResult {
        val response = networkClient.doTrackSearchRequest(TrackSearchRequest(expression))

        if(response.resultStatus == SearchStatus.RESPONSE_RECEIVED) {
            var tracks2: List<Track> = (response as TrackSearchResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = if (it.trackName.isNullOrEmpty()) "unknown" else it.trackName,
                    artistName = if (it.artistName.isNullOrEmpty()) "unknown" else it.artistName,
                    trackTimeMillis = it.trackTimeMillis,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = if (it.collectionName.isNullOrEmpty()) "unknown" else it.collectionName,
                    releaseDate = if (it.releaseDate.isNullOrEmpty()) "unknown" else it.releaseDate,
                    primaryGenreName = if (it.primaryGenreName.isNullOrEmpty()) "unknown" else it.primaryGenreName,
                    previewUrl = if(it.previewUrl.isNullOrEmpty()) "previewUrl is absent" else it.previewUrl,
                    country = if (it.country.isNullOrEmpty()) "unknown" else it.country
                )
            }
            val result2: TrackSearchResult = TrackSearchResult(tracks2)
            result2.resultStatus =  SearchStatus.RESPONSE_RECEIVED
            return  result2
        } else {
            val result2: TrackSearchResult = TrackSearchResult(emptyList())
            result2.resultStatus =  SearchStatus.NETWORK_ERROR
            return  result2
        }
    }
}