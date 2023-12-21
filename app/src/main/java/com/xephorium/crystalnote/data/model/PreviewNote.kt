package com.xephorium.crystalnote.data.model

import java.util.Date


/* A smaller, more lightweight version of the Note data class,
 * PreviewNote contains all of Note's fields except 'contents'
 * and is used to populate most of the screens in the app.
 */
data class PreviewNote(
    var id: Int,
    var name: String,
    var preview: String,
    var date: Date,
    var color: Int,
    var password: String
)
