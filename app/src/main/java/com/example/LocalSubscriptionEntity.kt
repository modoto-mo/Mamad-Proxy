package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_subscriptions")
data class LocalSubscriptionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
