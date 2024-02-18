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

class LibraryFavoriteFragmentViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var dbJob: Job? = null

    private val favoriteTracks: MutableLiveData<List<Track>> =
        MutableLiveData(ArrayList<Track>())

    fun getFavoriteTracks(): LiveData<List<Track>> =
        favoriteTracks  //Получаем историю залайканых треков


    fun onCreate() {
        dbJob = viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.getAllFavoriteTracks().collect { it ->
                it.reversed()
                favoriteTracks.postValue(it)
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob = viewModelScope.launch(Dispatchers.IO) {
                isClickAllowed = false
                delay(LibraryFavoriteFragmentViewModel.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun onDestroy() {
        clickJob?.cancel()
        dbJob?.cancel()
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}