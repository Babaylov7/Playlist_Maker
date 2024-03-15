package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.playlist.models.PlayList
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListDbConvertor {

    fun map(playList: PlayListEntity): PlayList {
        return PlayList(
            playList.id,
            playList.playListName,
            playList.playListDescription,
            playList.playListImage,
            createTracksFromJson(playList.tracksId),
            playList.tracksCount
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playList.id,
            playList.playlistName,
            playList.playlistDescription,
            playList.playlistImage,
            createJsonFromTracks(playList.tracks),
            playList.tracksCount
        )
    }

    private fun createTracksFromJson(json: String?): ArrayList<Track> {
        if (json.isNullOrEmpty()) {
            return ArrayList()
        } else {
            val token = object : TypeToken<ArrayList<Track>>() {}.type
            return Gson().fromJson<ArrayList<Track>>(json, token)
        }

    }

    private fun createJsonFromTracks(tracks: ArrayList<Track>): String? {
        if (tracks.isEmpty()) {
            return null
        } else {
            return Gson().toJson(tracks)
        }
    }
}