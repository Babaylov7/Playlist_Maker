package com.example.playlistmaker.presentation

import com.example.playlistmaker.domain.models.Track

interface ClickListenerForRecyclerView {
    fun onClick(track: Track)
}