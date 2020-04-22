package com.aljon.purrito.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aljon.purrito.data.domain.Feed

@Database(entities = [Feed::class], version = 1)
abstract class PurritoDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao

}