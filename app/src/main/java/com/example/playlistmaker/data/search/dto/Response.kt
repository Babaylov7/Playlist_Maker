package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.domain.search.models.SearchStatus

open class Response() {
    var resultStatus: SearchStatus = SearchStatus.DEFAULT
}