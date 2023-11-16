package com.example.playlistmaker.data.search.local.impl

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.app.AppSharedPreferences
import com.example.playlistmaker.data.search.local.SearchHistoryRepository
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(val app: AppSharedPreferences) : SearchHistoryRepository {

    private var sharedPrefs: SharedPreferences = app.getSharedPreferences(SEARCH_HISTORY, MODE_PRIVATE)
    private val tracks: ArrayList<Track> = readSearchHistory()       //Список треков в SP или пустой массив

    private fun readSearchHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(SEARCH_HISTORY, null) ?: return ArrayList<Track>()
        return createTracksFromJson(json)
    }

    override fun addTrack(track: Track) {
        if (tracks.isEmpty()) {
            tracks.add(track)
            writeSearchHistory(tracks)
            return
        }
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.trackId.equals(track.trackId)) {
                    tracks.remove(item)
                    tracks.add(0, track)
                    writeSearchHistory(tracks)
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

        writeSearchHistory(tracks)            //Проверили на наличие и записали все SP
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return tracks
    }

    override fun clean() {                  //Очистили массив, все записали в SP
        tracks.clear()
        writeSearchHistory(tracks)
    }


    private fun writeSearchHistory(tracks: ArrayList<Track>) {
        val json = createJsonFromTracks(tracks)
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    private fun createTracksFromJson(json: String): ArrayList<Track> {
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson<ArrayList<Track>>(json, token)
    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private companion object {
        const val MAX_LENGTH_OF_ARRAY = 10
        private const val SEARCH_HISTORY = "search_history"
    }

}