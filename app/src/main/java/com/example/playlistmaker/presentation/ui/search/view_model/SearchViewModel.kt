package com.example.playlistmaker.presentation.ui.search.view_model

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.models.SearchStatus
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.search.models.TrackSearchResult
import com.example.playlistmaker.presentation.ui.player.activity.PlayerActivity
import java.util.function.Consumer

class SearchViewModel(val context: Context) : ViewModel(), Consumer<TrackSearchResult> {
    private var requestText = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    //private var tracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())

    private var searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private var trackInteractor = Creator.provideTrackInteractor()
    private var tracksHistory = searchHistoryInteractor.getTracksHistory()



    private var foundTracks: MutableLiveData<TrackSearchResult> = MutableLiveData(TrackSearchResult(results = emptyList()))

    private val searchRunnable = Runnable {
        foundTracks.postValue(getLoadingStatus())
        sendRequest()
    }

    fun getLoadingStatus(): TrackSearchResult{
        var loading = TrackSearchResult(results = emptyList())
        loading.resultStatus = SearchStatus.LOADING
        return loading
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

    fun startPlayerActivity(track: Track) {              //Запустили активити с плеером
        Intent(context, PlayerActivity::class.java).apply {
            putExtra("track", track)
            context.startActivity(this)
        }
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

    fun cleanTracks() {
        //foundTracks.value = TrackSearchResult(results = emptyList())
    }

    fun sendRequest() {
        trackInteractor.searchTracks(requestText, this)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    override fun accept(trackSearchResult: TrackSearchResult) {
        when (trackSearchResult.resultStatus) {
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