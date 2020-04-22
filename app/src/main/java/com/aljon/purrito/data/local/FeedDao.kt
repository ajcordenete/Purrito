package com.aljon.purrito.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aljon.purrito.data.domain.Feed

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun favorite(feed: Feed)

    @Query("DELETE FROM Feeds WHERE id= :id")
    fun removeFavorite(id: String)

    @Query("SELECT * FROM Feeds ORDER BY `key` DESC")
    fun getAllFavorites() : LiveData<List<Feed>>

    @Query("SELECT COUNT(id) FROM Feeds WHERE id= :id")
    fun isExisting(id: String) : Int

}