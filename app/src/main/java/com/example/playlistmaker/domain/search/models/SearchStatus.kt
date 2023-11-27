package com.example.playlistmaker.domain.search.models

enum class SearchStatus {
    DEFAULT,
    RESPONSE_RECEIVED,
    LIST_IS_EMPTY,
    NETWORK_ERROR,
    LOADING
}