package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
): FavoriteTracksRepository {
    override fun insertTrackToFavorite(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().insertTrackToFavorite(trackEntity)
    }

    override fun deleteTrackFromFavorite(track: Track) {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getAllFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}