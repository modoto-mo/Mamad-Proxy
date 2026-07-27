package com.example

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "local_proxies",
    primaryKeys = ["rawUrl", "subId"],
    foreignKeys = [
        ForeignKey(
            entity = LocalSubscriptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["subId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subId"])]
)
data class LocalProxyEntity(
    val rawUrl: String,
    val server: String,
    val port: Int,
    val secret: String,
    val subId: String,
    val pingMs: Int? = null,
    val addedAt: Long = System.currentTimeMillis()
)

