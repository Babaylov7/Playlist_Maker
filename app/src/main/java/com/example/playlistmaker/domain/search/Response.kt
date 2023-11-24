package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.models.SearchStatus

open class Response() {
    var resultStatus: SearchStatus = SearchStatus.DEFAULT
}