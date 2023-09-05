package com.example.playlistmaker

import android.content.res.Configuration
import android.content.res.Resources

class ModeDefinition(val resources: Resources) {
    var mode: Boolean = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }

}