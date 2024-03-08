package com.example.playlistmaker.presentation.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryFavoriteViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var dbJob: Job? = null

    private val _favoriteTracks: MutableLiveData<List<Track>> =
        MutableLiveData(ArrayList<Track>())

    fun favoriteTracks(): LiveData<List<Track>> =
        _favoriteTracks  //Получаем историю залайканых треков


    fun onCreate() {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.getAllFavoriteTracks().collect { it ->
                _favoriteTracks.postValue(it)
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch(Dispatchers.IO) {
                isClickAllowed = false
                delay(LibraryFavoriteViewModel.CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun onDestroy() {
        clickJob?.cancel()
        dbJob?.cancel()
        isClickAllowed = true
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}