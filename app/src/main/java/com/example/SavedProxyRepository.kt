package com.example

import kotlinx.coroutines.flow.Flow

class SavedProxyRepository(private val savedProxyDao: SavedProxyDao) {

    val allSavedProxies: Flow<List<SavedProxyEntity>> = savedProxyDao.getAllSavedProxies()

    suspend fun saveProxy(proxy: SavedProxyEntity) {
        savedProxyDao.insertSavedProxy(proxy)
    }

    suspend fun unsaveProxy(rawUrl: String) {
        savedProxyDao.deleteSavedProxyByUrl(rawUrl)
    }

    fun isProxySaved(rawUrl: String): Flow<Boolean> {
        return savedProxyDao.isProxySaved(rawUrl)
    }
}
