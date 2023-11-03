package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.domain.models.Track


class SearchHistory(
    val sharedPreferences: AppSharedPreferences
) {

    private val tracks: ArrayList<Track> = getArrayOfTracks()       //Список треков в SP или пустой массив

    private fun getArrayOfTracks(): ArrayList<Track> {
        return sharedPreferences.readSearchHistory()
    }

    fun addTrack(track: Track) {
        if (tracks.isEmpty()) {
            tracks.add(track)
            sharedPreferences.writeSearchHistory(tracks)
            return
        }
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.trackId.equals(track.trackId)) {
                    tracks.remove(item)
                    tracks.add(0, track)
                    sharedPreferences.writeSearchHistory(tracks)
                    return
                }
            }
        }
        if (tracks.size < MAX_LENGTH_OF_ARRAY) {
            tracks.add(0, track)
        } else {
            tracks.removeLast()
            tracks.add(0, track)
        }

        sharedPreferences.writeSearchHistory(tracks)            //Проверили на наличие и записали все SP
    }

    fun getTracksHistory(): ArrayList<Track>{
        return tracks
    }

    fun clean() {                                                //Очистили массив, все записали в SP
        tracks.clear()
        sharedPreferences.writeSearchHistory(tracks)
    }

    private companion object {
        const val MAX_LENGTH_OF_ARRAY = 10
    }
}