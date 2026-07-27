package com.example

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedProxyDao {
    @Query("SELECT * FROM saved_proxies ORDER BY timestamp DESC")
    fun getAllSavedProxies(): Flow<List<SavedProxyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedProxy(proxy: SavedProxyEntity)

    @Query("DELETE FROM saved_proxies WHERE rawUrl = :rawUrl")
    suspend fun deleteSavedProxyByUrl(rawUrl: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_proxies WHERE rawUrl = :rawUrl LIMIT 1)")
    fun isProxySaved(rawUrl: String): Flow<Boolean>
}
