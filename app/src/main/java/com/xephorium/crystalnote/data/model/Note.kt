package com.xephorium.crystalnote.data.model

import java.util.Date

data class Note(
        var color: Int,
        var date: Date,
        var name: String,
        var path: String,
        var preview: String
)
