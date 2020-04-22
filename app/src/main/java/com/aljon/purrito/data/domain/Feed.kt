package com.aljon.purrito.data.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Feeds")
data class Feed (

    @ColumnInfo(name = "id") val id: String,

    @ColumnInfo( name = "url") val url: String,

    @ColumnInfo(name = "type") val type: String = "",

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key") val key: Int = 0


)