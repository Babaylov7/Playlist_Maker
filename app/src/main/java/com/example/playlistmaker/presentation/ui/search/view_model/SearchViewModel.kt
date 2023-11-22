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
    private val searchRunnable = Runnable { sendRequest() }
    private var tracks: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    private var searchStatus: MutableLiveData<SearchStatus> = MutableLiveData(SearchStatus.DEFAULT)
    private var progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    private var searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private var trackInteractor = Creator.provideTrackInteractor()
    private var tracksHistory = searchHistoryInteractor.getTracksHistory()


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun changeRequestText(text: String) {
        requestText = text
    }

    fun getTracks(): LiveData<List<Track>> =
        tracks        //Получаем LiveData со списком треков

    fun getSearchStatus(): LiveData<SearchStatus> =
        searchStatus    //Получаем статус запроса треков из сети

    fun getTracksHistory(): ArrayList<Track> =
        tracksHistory    //Получаем историю прослушанных треков

    fun getProgressBarVisibility(): LiveData<Boolean> =
        progressBarVisibility

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
        tracks.postValue(emptyList())
    }

    fun sendRequest() {
        progressBarVisibility.value = true
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
        progressBarVisibility.postValue(false)
        tracks.postValue(emptyList())
        when (trackSearchResult.resultStatus) {
            SearchStatus.RESPONSE_RECEIVED -> {
                tracks.postValue(trackSearchResult.results)
            }

            SearchStatus.LIST_IS_EMPTY, SearchStatus.NETWORK_ERROR, SearchStatus.DEFAULT -> {
            }
        }
    }

    private companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}