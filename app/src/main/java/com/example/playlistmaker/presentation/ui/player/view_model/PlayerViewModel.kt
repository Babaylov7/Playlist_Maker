package com.example.playlistmaker.presentation.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.player.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.models.MediaPlayerStatus
import com.example.playlistmaker.domain.player.models.PlayerProgressStatus
import com.example.playlistmaker.domain.playlist.PlayList
import com.example.playlistmaker.domain.search.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val playListInteractor: PlayListInteractor
) :
    ViewModel() {
    private var updateTimeOfPlayJob: Job? = null
    private var favoriteButtonJob: Job? = null
    private var addTrackInDb: Job? = null
    private var deleteTrackFromDb: Job? = null

    private val playerProgressStatus: MutableLiveData<PlayerProgressStatus> =       //Лайф дата со статусом плеера
        MutableLiveData(updatePlayerProgressStatus())
    fun getPlayerProgressStatus(): LiveData<PlayerProgressStatus> = playerProgressStatus

    private var trackAddInFavorite: MutableLiveData<Boolean> = MutableLiveData(false)       //Лайф дата с лайками
    fun favoriteStatus(): LiveData<Boolean> = trackAddInFavorite

    private val playListsLiveData: MutableLiveData<List<PlayList>> =            //Лайф дата с плейлистами
        MutableLiveData<List<PlayList>>()
    fun getPlayLists(): LiveData<List<PlayList>> = playListsLiveData

    private val toastMessage: MutableLiveData<String> = MutableLiveData<String>()   //Лайф Дата с сообщением для тоста
    fun getToastMessage(): LiveData<String> = toastMessage


    fun onCreate(track: Track) {
        mediaPlayerInteractor.preparePlayer(track)
        playerProgressStatus.value = updatePlayerProgressStatus()
        trackAddInFavorite.postValue(track.isFavorite)
    }

    private fun updatePlayerProgressStatus(): PlayerProgressStatus {
        return mediaPlayerInteractor.getPlayerProgressStatus()
    }

    fun pauseMediaPlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun destroyMediaPlayer() {
        updateTimeOfPlayJob?.cancel()
        favoriteButtonJob?.cancel()
        addTrackInDb?.cancel()
        deleteTrackFromDb?.cancel()
        mediaPlayerInteractor.destroyPlayer()
    }

    private fun updateTimeOfPlay() {                   //Обновленеи времени проигрования трека
        playerProgressStatus.value = updatePlayerProgressStatus()

        when (playerProgressStatus.value!!.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                updateTimeOfPlayJob = viewModelScope.launch {
                    delay(UPDATE)
                    updateTimeOfPlay()
                }
            }

            else -> {
                updateTimeOfPlayJob?.cancel()
            }
        }
    }

    fun playbackControl() {
        playerProgressStatus.value = updatePlayerProgressStatus()
        when (playerProgressStatus.value!!.mediaPlayerStatus) {
            MediaPlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }

            MediaPlayerStatus.STATE_PREPARED, MediaPlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }

            MediaPlayerStatus.STATE_ERROR, MediaPlayerStatus.STATE_DEFAULT -> {
            }
        }
    }

    fun clickButtonFavorite(track: Track){
        if(trackAddInFavorite.value!!){
            track.isFavorite = false
            //удалить и изм значение лайф даты
            deleteTrackFromDb = viewModelScope.launch {
                favoriteTracksInteractor.deleteTrackFromFavorite(track)
            }
            trackAddInFavorite.postValue(false)
            searchHistoryInteractor.addTrack(track)
        }
        else {
            track.isFavorite = true
            addTrackInDb = viewModelScope.launch {
                favoriteTracksInteractor.insertTrackToFavorite(track)
            }
            trackAddInFavorite.postValue(true)
            searchHistoryInteractor.addTrack(track)
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        updateTimeOfPlay()
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        playerProgressStatus.value = updatePlayerProgressStatus()
    }

    fun checkPlayListsInDb() {
        viewModelScope.launch {
            playListInteractor
                .getPlayLists()
                .collect { result ->
                    playListsLiveData.postValue(result)
                }
        }
    }

    fun addTrackInPlayList(playList: PlayList, track: Track){
        if(playList.tracks.contains(track)){
            toastMessage.value = "Трек уже добавлен в плейлист ${playList.playlistName}"
        } else {
            val tracks = playList.tracks
            tracks.add(track)
            viewModelScope.launch {
                playListInteractor.updateTracks(playList.id, tracks, playList.tracksCount + 1)
            }
            toastMessage.value = "Добавлено в плейлист ${playList.playlistName}"
        }
    }


    companion object {
        private const val UPDATE = 250L
    }
}