package com.example

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetSocketAddress
import java.net.Socket
import java.util.UUID

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English (United States)"),
    PERSIAN("fa", "فارسی (Persian)")
}

class ProxyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = SavedProxyRepository(database.savedProxyDao())

    val savedProxies: StateFlow<List<SavedProxyEntity>> = repository.allSavedProxies
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _savedProxiesList = MutableStateFlow<List<ProxyItem>>(emptyList())
    val savedProxiesList: StateFlow<List<ProxyItem>> = _savedProxiesList.asStateFlow()

    private val _isTestingAllSaved = MutableStateFlow(false)
    val isTestingAllSaved: StateFlow<Boolean> = _isTestingAllSaved.asStateFlow()

    private val client = OkHttpClient()

    private val _proxies = MutableStateFlow<List<ProxyItem>>(emptyList())
    val proxies: StateFlow<List<ProxyItem>> = _proxies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isAutoSortEnabled = MutableStateFlow(true)
    val isAutoSortEnabled: StateFlow<Boolean> = _isAutoSortEnabled.asStateFlow()

    private val _isTestingAll = MutableStateFlow(false)
    val isTestingAll: StateFlow<Boolean> = _isTestingAll.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterType = MutableStateFlow("ALL") // ALL, ONLINE, FAST
    val filterType: StateFlow<String> = _filterType.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.PERSIAN)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _proxyTimeoutMs = MutableStateFlow(700)
    val proxyTimeoutMs: StateFlow<Int> = _proxyTimeoutMs.asStateFlow()

    private val localDao = database.localProxyDao()

    val subscriptions: StateFlow<List<LocalSubscriptionEntity>> = localDao.getAllSubscriptions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedSubscriptionId = MutableStateFlow<String?>(null)
    val selectedSubscriptionId: StateFlow<String?> = _selectedSubscriptionId.asStateFlow()

    private val _localProxies = MutableStateFlow<List<ProxyItem>>(emptyList())
    val localProxies: StateFlow<List<ProxyItem>> = _localProxies.asStateFlow()

    private val _isTestingAllLocal = MutableStateFlow(false)
    val isTestingAllLocal: StateFlow<Boolean> = _isTestingAllLocal.asStateFlow()

    private val _isLocalAutoSortEnabled = MutableStateFlow(true)
    val isLocalAutoSortEnabled: StateFlow<Boolean> = _isLocalAutoSortEnabled.asStateFlow()

    private var autoSortJob: Job? = null

    init {
        fetchProxies()
        startAutoSortLoop()
        observeSavedProxies()
        initLocalSubscriptions()
    }

    private fun initLocalSubscriptions() {
        viewModelScope.launch(Dispatchers.IO) {
            // Delete old QR sub if present
            localDao.deleteSubscription("qr_default_sub")

            val count = localDao.getSubscriptionCount()
            if (count == 0) {
                val defaultSub = LocalSubscriptionEntity(
                    id = DefaultLocalProxies.DEFAULT_SUB_ID,
                    name = DefaultLocalProxies.DEFAULT_SUB_NAME
                )
                localDao.insertSubscription(defaultSub)

                val parsed = DefaultLocalProxies.RAW_TXT.lines()
                    .mapIndexedNotNull { index, line ->
                        ProxyItem.parseFromLine(UUID.randomUUID().toString(), line)
                    }
                val entities = parsed.map { p ->
                    LocalProxyEntity(
                        rawUrl = p.rawUrl,
                        server = p.server,
                        port = p.port,
                        secret = p.secret,
                        subId = DefaultLocalProxies.DEFAULT_SUB_ID
                    )
                }
                localDao.insertProxies(entities)
            }

            subscriptions.collect { subs ->
                if (subs.isNotEmpty()) {
                    val currentSel = _selectedSubscriptionId.value
                    if (currentSel == null || subs.none { it.id == currentSel }) {
                        _selectedSubscriptionId.value = subs.first().id
                    }
                } else {
                    _selectedSubscriptionId.value = null
                    _localProxies.value = emptyList()
                }
            }
        }

        viewModelScope.launch {
            _selectedSubscriptionId.collectLatest { subId ->
                if (subId != null) {
                    localDao.getProxiesForSubscription(subId).collect { proxyEntities ->
                        val currentList = _localProxies.value
                        val mapped = proxyEntities.map { entity ->
                            val existing = currentList.find { it.rawUrl == entity.rawUrl }
                            existing?.copy(
                                isTesting = existing.isTesting
                            ) ?: ProxyItem(
                                id = UUID.randomUUID().toString(),
                                rawUrl = entity.rawUrl,
                                server = entity.server,
                                port = entity.port,
                                secret = entity.secret,
                                pingMs = null
                            )
                        }
                        _localProxies.value = mapped.sortedWith { a, b -> compareProxies(a, b) }
                    }
                } else {
                    _localProxies.value = emptyList()
                }
            }
        }
    }

    fun selectSubscription(subId: String) {
        _selectedSubscriptionId.value = subId
    }

    fun createSubscription(name: String, rawContent: String, onComplete: ((String, Int) -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val subId = UUID.randomUUID().toString()
            val subName = name.ifBlank { "ساب جدید" }
            val sub = LocalSubscriptionEntity(id = subId, name = subName)
            localDao.insertSubscription(sub)

            val parsed = rawContent.lines()
                .mapIndexedNotNull { index, line ->
                    ProxyItem.parseFromLine(UUID.randomUUID().toString(), line)
                }
            val entities = parsed.map { p ->
                LocalProxyEntity(
                    rawUrl = p.rawUrl,
                    server = p.server,
                    port = p.port,
                    secret = p.secret,
                    subId = subId
                )
            }
            localDao.insertProxies(entities)
            _selectedSubscriptionId.value = subId
            withContext(Dispatchers.Main) {
                onComplete?.invoke(subId, parsed.size)
            }
        }
    }

    fun importProxiesToSub(subId: String, rawContent: String, onComplete: (Int) -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val parsed = rawContent.lines()
                .mapIndexedNotNull { index, line ->
                    ProxyItem.parseFromLine(UUID.randomUUID().toString(), line)
                }
            if (parsed.isNotEmpty()) {
                val entities = parsed.map { p ->
                    LocalProxyEntity(
                        rawUrl = p.rawUrl,
                        server = p.server,
                        port = p.port,
                        secret = p.secret,
                        subId = subId
                    )
                }
                localDao.insertProxies(entities)
                _selectedSubscriptionId.value = subId
                withContext(Dispatchers.Main) {
                    onComplete(parsed.size)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onComplete(0)
                }
            }
        }
    }

    fun deleteSubscription(subId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localDao.deleteSubscription(subId)
            val currentSubs = subscriptions.value.filter { it.id != subId }
            if (currentSubs.isNotEmpty()) {
                _selectedSubscriptionId.value = currentSubs.first().id
            } else {
                _selectedSubscriptionId.value = null
            }
        }
    }

    fun exportSubscription(subId: String, onResult: (subName: String, content: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val subs = subscriptions.value
            val sub = subs.find { it.id == subId } ?: return@launch
            val proxies = localDao.getProxiesListForSubscription(subId)
            val exportText = proxies.joinToString("\n") { it.rawUrl }
            withContext(Dispatchers.Main) {
                onResult(sub.name, exportText)
            }
        }
    }

    fun testLocalProxyPing(proxyId: String) {
        viewModelScope.launch {
            val currentList = _localProxies.value
            _localProxies.value = currentList.map {
                if (it.id == proxyId) it.copy(isTesting = true) else it
            }
            val proxy = _localProxies.value.find { it.id == proxyId } ?: return@launch
            val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
            val updated = _localProxies.value.map {
                if (it.id == proxyId) it.copy(pingMs = ping, isTesting = false) else it
            }
            _localProxies.value = updated.sortedWith { a, b -> compareProxies(a, b) }
        }
    }

    fun testAllLocalProxies() {
        if (_isTestingAllLocal.value) return
        viewModelScope.launch {
            _isTestingAllLocal.value = true
            val currentList = _localProxies.value
            _localProxies.value = currentList.map { it.copy(isTesting = true) }

            val semaphore = Semaphore(5)
            val jobs = currentList.map { proxy ->
                launch(Dispatchers.Default) {
                    semaphore.withPermit {
                        val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
                        withContext(Dispatchers.Main) {
                            val updated = _localProxies.value.map {
                                if (it.id == proxy.id) it.copy(pingMs = ping, isTesting = false) else it
                            }
                            _localProxies.value = updated.sortedWith { a, b -> compareProxies(a, b) }
                        }
                    }
                }
            }
            jobs.forEach { it.join() }
            if (_isLocalAutoSortEnabled.value) {
                sortLocalProxiesInternal()
            }
            _isTestingAllLocal.value = false
        }
    }

    private fun observeSavedProxies() {
        viewModelScope.launch {
            repository.allSavedProxies.collect { entities ->
                val currentSavedList = _savedProxiesList.value
                val newList = entities.map { entity ->
                    val existing = currentSavedList.find { it.rawUrl == entity.rawUrl }
                    existing ?: ProxyItem(
                        id = UUID.randomUUID().toString(),
                        rawUrl = entity.rawUrl,
                        server = entity.server,
                        port = entity.port,
                        secret = entity.secret
                    )
                }
                _savedProxiesList.value = newList
            }
        }
    }

    fun toggleSaveProxy(proxy: ProxyItem) {
        viewModelScope.launch {
            val isSaved = savedProxies.value.any { it.rawUrl == proxy.rawUrl }
            if (isSaved) {
                repository.unsaveProxy(proxy.rawUrl)
            } else {
                repository.saveProxy(
                    SavedProxyEntity(
                        rawUrl = proxy.rawUrl,
                        server = proxy.server,
                        port = proxy.port,
                        secret = proxy.secret
                    )
                )
            }
        }
    }

    fun unsaveProxy(rawUrl: String) {
        viewModelScope.launch {
            repository.unsaveProxy(rawUrl)
        }
    }

    fun testSavedProxyPing(proxyId: String) {
        viewModelScope.launch {
            val currentList = _savedProxiesList.value
            _savedProxiesList.value = currentList.map {
                if (it.id == proxyId) it.copy(isTesting = true) else it
            }
            val proxy = _savedProxiesList.value.find { it.id == proxyId } ?: return@launch
            val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
            _savedProxiesList.value = _savedProxiesList.value.map {
                if (it.id == proxyId) it.copy(pingMs = ping, isTesting = false) else it
            }
        }
    }

    fun testAllSavedProxies() {
        if (_isTestingAllSaved.value) return
        viewModelScope.launch {
            _isTestingAllSaved.value = true
            val currentList = _savedProxiesList.value
            _savedProxiesList.value = currentList.map { it.copy(isTesting = true) }

            val semaphore = Semaphore(5)
            val jobs = currentList.map { proxy ->
                launch(Dispatchers.Default) {
                    semaphore.withPermit {
                        val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
                        withContext(Dispatchers.Main) {
                            _savedProxiesList.value = _savedProxiesList.value.map {
                                if (it.id == proxy.id) it.copy(pingMs = ping, isTesting = false) else it
                            }
                        }
                    }
                }
            }
            jobs.forEach { it.join() }
            _isTestingAllSaved.value = false
        }
    }

    fun loadSettings(context: Context) {
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        _isDarkMode.value = prefs.getBoolean("is_dark_mode", true)
        val langCode = prefs.getString("language_code", "fa") ?: "fa"
        _language.value = if (langCode == "en") AppLanguage.ENGLISH else AppLanguage.PERSIAN
        _proxyTimeoutMs.value = prefs.getInt("proxy_timeout_ms", 700)
    }

    fun setProxyTimeoutMs(context: Context, timeoutMs: Int) {
        val validTimeout = timeoutMs.coerceIn(100, 10000)
        _proxyTimeoutMs.value = validTimeout
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .edit()
            .putInt("proxy_timeout_ms", validTimeout)
            .apply()
        viewModelScope.launch(Dispatchers.Default) {
            sortProxiesInternal()
            sortLocalProxiesInternal()
        }
    }

    fun toggleDarkMode(context: Context) {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_dark_mode", newValue)
            .apply()
    }

    fun setLanguage(context: Context, language: AppLanguage) {
        _language.value = language
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .edit()
            .putString("language_code", language.code)
            .apply()
    }

    fun fetchProxies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = withContext(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("https://c-mamad.ir/mm/mm2/mm3/index.txt")
                        .build()
                    
                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) throw Exception("Unexpected code $response")
                        val bodyString = response.body?.string() ?: ""
                        
                        bodyString.lines()
                            .mapIndexedNotNull { index, line ->
                                ProxyItem.parseFromLine(UUID.randomUUID().toString(), line)
                            }
                    }
                }
                _proxies.value = list
            } catch (e: Exception) {
                e.printStackTrace()
                // Keep existing or empty
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun testProxyPing(proxyId: String) {
        viewModelScope.launch {
            updateProxyTestingState(proxyId, true)
            val proxy = _proxies.value.find { it.id == proxyId } ?: return@launch
            val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
            updateProxyPingResult(proxyId, ping)
        }
    }

    fun testAllProxies() {
        if (_isTestingAll.value) return
        viewModelScope.launch {
            _isTestingAll.value = true
            val currentList = _proxies.value
            
            // Mark all as testing
            _proxies.value = currentList.map { it.copy(isTesting = true) }

            // Bounded concurrency using Semaphore to test up to 10 in parallel
            val semaphore = Semaphore(10)
            
            val jobs = currentList.map { proxy ->
                launch(Dispatchers.Default) {
                    semaphore.withPermit {
                        val ping = runPingCheck(proxy.server, proxy.port, proxy.secret)
                        withContext(Dispatchers.Main) {
                            updateProxyPingResult(proxy.id, ping)
                        }
                    }
                }
            }
            
            jobs.forEach { it.join() }
            _isTestingAll.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterType(type: String) {
        _filterType.value = type
    }

    fun toggleAutoSort() {
        _isAutoSortEnabled.value = !_isAutoSortEnabled.value
        if (_isAutoSortEnabled.value) {
            sortProxiesByPing()
        }
    }

    fun toggleLocalAutoSort() {
        _isLocalAutoSortEnabled.value = !_isLocalAutoSortEnabled.value
        if (_isLocalAutoSortEnabled.value) {
            sortLocalProxiesByPing()
        }
    }

    fun sortProxiesByPing() {
        viewModelScope.launch(Dispatchers.Default) {
            sortProxiesInternal()
        }
    }

    fun sortLocalProxiesByPing() {
        viewModelScope.launch(Dispatchers.Default) {
            sortLocalProxiesInternal()
        }
    }

    private fun startAutoSortLoop() {
        autoSortJob?.cancel()
        autoSortJob = viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(1000)
                if (_isAutoSortEnabled.value) {
                    sortProxiesInternal()
                }
                if (_isLocalAutoSortEnabled.value) {
                    sortLocalProxiesInternal()
                }
            }
        }
    }

    private suspend fun sortProxiesInternal() {
        val currentList = _proxies.value
        val sortedList = currentList.sortedWith { a, b -> compareProxies(a, b) }
        if (currentList != sortedList) {
            withContext(Dispatchers.Main) {
                _proxies.value = sortedList
            }
        }
    }

    private suspend fun sortLocalProxiesInternal() {
        val currentList = _localProxies.value
        val sortedList = currentList.sortedWith { a, b -> compareProxies(a, b) }
        if (currentList != sortedList) {
            withContext(Dispatchers.Main) {
                _localProxies.value = sortedList
            }
        }
    }

    private fun compareProxies(a: ProxyItem, b: ProxyItem): Int {
        val timeout = _proxyTimeoutMs.value.toLong()
        val aPing = a.pingMs
        val bPing = b.pingMs

        val aStatus = when {
            aPing == null -> 1 // Untested (Middle)
            aPing < 0L || aPing >= timeout -> 2 // Offline / Cut off (Bottom)
            else -> 0 // Online with valid ping (Top)
        }
        val bStatus = when {
            bPing == null -> 1 // Untested (Middle)
            bPing < 0L || bPing >= timeout -> 2 // Offline / Cut off (Bottom)
            else -> 0 // Online with valid ping (Top)
        }

        if (aStatus != bStatus) {
            return aStatus.compareTo(bStatus)
        }

        // Both in same status category
        if (aStatus == 0) {
            // Both online: compare pings (lowest ping first)
            val pingComp = aPing!!.compareTo(bPing!!)
            if (pingComp != 0) return pingComp
        } else if (aStatus == 2) {
            // Both offline/cut off: sort numerical pings (e.g. 800ms) before total connection failures (-1L)
            val aVal = if (aPing!! < 0L) Long.MAX_VALUE else aPing
            val bVal = if (bPing!! < 0L) Long.MAX_VALUE else bPing
            val comp = aVal.compareTo(bVal)
            if (comp != 0) return comp
        }

        return a.id.compareTo(b.id)
    }

    private suspend fun runPingCheck(server: String, port: Int, secret: String, timeoutMs: Int = _proxyTimeoutMs.value): Long {
        return withContext(Dispatchers.IO) {
            val start = System.currentTimeMillis()
            var socket: Socket? = null
            try {
                socket = Socket()
                socket.soTimeout = timeoutMs
                // Socket address handles resolution and connect
                socket.connect(InetSocketAddress(server, port), timeoutMs)
                
                val secretBytes = parseSecret(secret)
                if (secretBytes != null && secretBytes.size == 16) {
                    val init = ByteArray(64)
                    val random = java.security.SecureRandom()
                    
                    // Generate valid obfuscated 64-byte payload
                    while (true) {
                        random.nextBytes(init)
                        val val0 = init[0].toInt() and 0xFF
                        if (val0 == 0xef) continue
                        
                        val firstInt = ((init[3].toInt() and 0xFF) shl 24) or
                                       ((init[2].toInt() and 0xFF) shl 16) or
                                       ((init[1].toInt() and 0xFF) shl 8) or
                                       (init[0].toInt() and 0xFF)
                        if (firstInt == 0xefefefef.toInt() ||
                            firstInt == 0x47455420 || // GET 
                            firstInt == 0x504f5354 || // POST
                            firstInt == 0x48454144 || // HEAD
                            firstInt == 0x20544547 || // GET reversed
                            firstInt == 0x54534f50 || // POST reversed
                            firstInt == 0x44414548    // HEAD reversed
                        ) continue
                        
                        val secondInt = ((init[7].toInt() and 0xFF) shl 24) or
                                        ((init[6].toInt() and 0xFF) shl 16) or
                                        ((init[5].toInt() and 0xFF) shl 8) or
                                        (init[4].toInt() and 0xFF)
                        if (secondInt == 0x00000000) continue
                        
                        break
                    }
                    
                    // Set abridged protocol identifier at index 56-59
                    init[56] = 0xef.toByte()
                    init[57] = 0xef.toByte()
                    init[58] = 0xef.toByte()
                    init[59] = 0xef.toByte()
                    
                    // Key derivation
                    val encryptKeyBytes = ByteArray(32)
                    System.arraycopy(init, 8, encryptKeyBytes, 0, 32)
                    val encryptKeySource = encryptKeyBytes + secretBytes
                    val encryptKey = sha256(encryptKeySource)
                    val encryptIv = ByteArray(16)
                    System.arraycopy(init, 40, encryptIv, 0, 16)
                    
                    // Derive decrypt parameters
                    val initRev = ByteArray(48)
                    for (i in 0 until 48) {
                        initRev[i] = init[55 - i]
                    }
                    val decryptKeyBytes = ByteArray(32)
                    System.arraycopy(initRev, 0, decryptKeyBytes, 0, 32)
                    val decryptKeySource = decryptKeyBytes + secretBytes
                    val decryptKey = sha256(decryptKeySource)
                    val decryptIv = ByteArray(16)
                    System.arraycopy(initRev, 32, decryptIv, 0, 16)
                    
                    // Initialize AES-CTR ciphers
                    val encryptCipher = javax.crypto.Cipher.getInstance("AES/CTR/NoPadding")
                    encryptCipher.init(
                        javax.crypto.Cipher.ENCRYPT_MODE,
                        javax.crypto.spec.SecretKeySpec(encryptKey, "AES"),
                        javax.crypto.spec.IvParameterSpec(encryptIv)
                    )
                    
                    // To properly advance the AES-CTR keystream by 56 bytes, we encrypt the entire
                    // 64-byte payload, and then restore the first 56 bytes to their unencrypted state.
                    val encrypted = encryptCipher.doFinal(init)
                    System.arraycopy(encrypted, 56, init, 56, 8)
                    
                    // Write the 64-byte payload to the socket
                    val outputStream = socket.getOutputStream()
                    outputStream.write(init)
                    outputStream.flush()
                    
                    // Read response from proxy (we expect 64 bytes back from an active MTProto proxy)
                    val inputStream = socket.getInputStream()
                    val response = ByteArray(64)
                    var totalRead = 0
                    while (totalRead < 64) {
                        val read = inputStream.read(response, totalRead, 64 - totalRead)
                        if (read == -1) {
                            throw java.io.IOException("Connection closed prematurely by MTProto proxy")
                        }
                        totalRead += read
                    }
                    
                    // If we successfully read 64 bytes of response, the proxy is active and reachable!
                    val end = System.currentTimeMillis()
                    end - start
                } else {
                    // Fallback to standard TCP ping if secret is not parsing
                    val end = System.currentTimeMillis()
                    end - start
                }
            } catch (e: Exception) {
                -1L
            } finally {
                try {
                    socket?.close()
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
    }

    private fun parseSecret(secret: String): ByteArray? {
        return try {
            var clean = secret.trim().lowercase()
            if (clean.startsWith("dd") || clean.startsWith("ee")) {
                clean = clean.substring(2)
            }
            // The actual MTProto key is always the first 32 hex characters (16 bytes)
            if (clean.length < 32) return null
            val hexKey = clean.substring(0, 32)
            val data = ByteArray(16)
            for (i in 0 until 16) {
                val index = i * 2
                data[i] = ((Character.digit(hexKey[index], 16) shl 4) + Character.digit(hexKey[index + 1], 16)).toByte()
            }
            data
        } catch (e: Exception) {
            null
        }
    }

    private fun sha256(data: ByteArray): ByteArray {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        return digest.digest(data)
    }

    private fun updateProxyTestingState(id: String, isTesting: Boolean) {
        _proxies.value = _proxies.value.map {
            if (it.id == id) it.copy(isTesting = isTesting) else it
        }
    }

    private fun updateProxyPingResult(id: String, pingMs: Long) {
        _proxies.value = _proxies.value.map {
            if (it.id == id) it.copy(pingMs = pingMs, isTesting = false) else it
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoSortJob?.cancel()
    }
}
