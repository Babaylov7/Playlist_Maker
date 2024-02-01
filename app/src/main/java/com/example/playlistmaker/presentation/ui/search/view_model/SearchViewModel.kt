package com.example.playlistmaker.presentation.ui.search.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.function.Consumer

class SearchViewModel(
    val searchHistoryInteractor: SearchHistoryInteractor,
    val trackInteractor: TrackInteractor
) : ViewModel(), Consumer<TrackSearchResult> {
    private var requestText = ""
    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var searchJob: Job? = null

    private var tracksHistory = searchHistoryInteractor.getTracksHistory()

    private val foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(results = emptyList(), SearchStatus.DEFAULT))

    fun getFoundTracks(): LiveData<TrackSearchResult> = foundTracks

    private fun search() {
        foundTracks.postValue(getLoadingStatus())
        sendRequest()
    }

    fun removeCallbacks() {
        searchJob?.cancel()
    }

    fun changeRequestText(text: String) {
        requestText = text
    }

    fun getTracksHistory(): ArrayList<Track> =
        tracksHistory    //Получаем историю прослушанных треков

    fun updateTrackHistory() {
        tracksHistory = searchHistoryInteractor.getTracksHistory()
    }

    fun addTrackInSearchHistory(track: Track) {          //Добавить трек в историю поиска
        searchHistoryInteractor.addTrack(track)
    }

    fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search()
        }
    }

    fun cleanHistory() {
        searchHistoryInteractor.clean()
    }

    fun deleteFoundTracks() {
        foundTracks.postValue(TrackSearchResult(results = emptyList(), SearchStatus.DEFAULT))
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch {
                isClickAllowed = false
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun sendRequest() {
        trackInteractor.searchTracks(requestText, this)
    }

    private fun getLoadingStatus(): TrackSearchResult {
        return TrackSearchResult(
            results = emptyList(),
            SearchStatus.LOADING
        )
    }

    override fun accept(trackSearchResult: TrackSearchResult) {
        when (trackSearchResult.status) {
            SearchStatus.RESPONSE_RECEIVED -> {
                foundTracks.postValue(trackSearchResult)
            }

            SearchStatus.LIST_IS_EMPTY, SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT -> {
                foundTracks.postValue(trackSearchResult)
            }

            SearchStatus.LOADING -> {
            }
        }
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}