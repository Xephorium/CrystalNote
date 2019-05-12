package com.xephorium.crystalnote.data.model

import java.util.Date

data class Note(
        var color: Int = 0,
        var date: Date? = null,
        var name: String? = null,
        var path: String? = null,
        var preview: String? = null
)
