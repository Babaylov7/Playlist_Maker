package com.example.playlistmaker


class SearchHistory(
    val sharedPreferences: AppSharedPreferences
) {

    val tracks: ArrayList<Track> = getArrayOfTracks()       //Список треков в SP или пустой массив

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
            for (ttt in tracks) {
                if (ttt.trackId.equals(track.trackId)) {
                    tracks.remove(ttt)
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

    fun clean() {                                                //Очистили массив, все записали в SP
        tracks.clear()
        sharedPreferences.writeSearchHistory(tracks)
    }

    private companion object {
        const val MAX_LENGTH_OF_ARRAY = 10
    }
}