package com.example.playlistmaker.presentation.ui.search.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator

class SearchViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(),
        trackInteractor = Creator.provideTrackInteractor()
        ) as T
    }
}