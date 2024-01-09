package com.example.playlistmaker.presentation.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import java.util.function.Consumer

class SearchViewModel(
    val searchHistoryInteractor: SearchHistoryInteractor,
    val trackInteractor: TrackInteractor
) : ViewModel(), Consumer<TrackSearchResult> {
    private var requestText = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var tracksHistory = searchHistoryInteractor.getTracksHistory()

    private val foundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(results = emptyList(), SearchStatus.DEFAULT))

    private val searchRunnable = Runnable {
        foundTracks.postValue(getLoadingStatus())
        sendRequest()
    }

    fun getFoundTracks(): LiveData<TrackSearchResult> = foundTracks

    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
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

    fun searchDebounce() {                                          //В момент вызова функции searchDebounce() мы удаляем
        handler.removeCallbacks(searchRunnable)                             //последнюю запланированную отправку запроса и тут же,
        handler.postDelayed(
            searchRunnable,
            SEARCH_DEBOUNCE_DELAY
        )                                                                   //используя метод postDelayed(), планируем запуск этого же
    }

    fun cleanHistory() {
        searchHistoryInteractor.clean()
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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