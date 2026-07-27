package com.example

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalProxyDao {
    @Query("SELECT * FROM local_subscriptions ORDER BY createdAt ASC")
    fun getAllSubscriptions(): Flow<List<LocalSubscriptionEntity>>

    @Query("SELECT COUNT(*) FROM local_subscriptions")
    suspend fun getSubscriptionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: LocalSubscriptionEntity)

    @Query("DELETE FROM local_subscriptions WHERE id = :subId")
    suspend fun deleteSubscription(subId: String)

    @Query("SELECT * FROM local_proxies WHERE subId = :subId ORDER BY addedAt DESC")
    fun getProxiesForSubscription(subId: String): Flow<List<LocalProxyEntity>>

    @Query("SELECT * FROM local_proxies WHERE subId = :subId ORDER BY addedAt DESC")
    suspend fun getProxiesListForSubscription(subId: String): List<LocalProxyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProxies(proxies: List<LocalProxyEntity>)

    @Query("UPDATE local_proxies SET pingMs = :pingMs WHERE rawUrl = :rawUrl AND subId = :subId")
    suspend fun updateProxyPing(rawUrl: String, subId: String, pingMs: Int)

    @Query("DELETE FROM local_proxies WHERE subId = :subId")
    suspend fun deleteProxiesForSubscription(subId: String)
}
