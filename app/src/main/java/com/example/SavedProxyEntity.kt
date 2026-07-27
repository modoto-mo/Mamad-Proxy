package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_proxies")
data class SavedProxyEntity(
    @PrimaryKey val rawUrl: String,
    val server: String,
    val port: Int,
    val secret: String,
    val timestamp: Long = System.currentTimeMillis()
)
