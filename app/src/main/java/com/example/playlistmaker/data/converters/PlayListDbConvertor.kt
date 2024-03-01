package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.playlist.PlayList

class PlayListDbConvertor {

    fun map(playList: PlayListEntity): PlayList {
        return PlayList(
            playList.id,
            playList.playListName,
            playList.playListDescription,
            playList.playListImage,
            playList.tracksId,
            playList.tracksCount
        )
    }

    fun map(playList: PlayList): PlayListEntity {
        return  PlayListEntity(
            playList.id,
            playList.playlistName,
            playList.playlistDescription,
            playList.playlistImage,
            playList.tracksId,
            playList.tracksCount
        )
    }
}