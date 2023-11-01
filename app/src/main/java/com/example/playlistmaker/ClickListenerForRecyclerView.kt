package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track

interface ClickListenerForRecyclerView {
    fun onClick(track: Track)
}