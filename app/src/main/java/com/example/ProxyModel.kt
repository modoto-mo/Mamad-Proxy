package com.example

import java.net.URLDecoder

data class ProxyItem(
    val id: String,
    val rawUrl: String,
    val server: String,
    val port: Int,
    val secret: String,
    val pingMs: Long? = null, // null = not tested, -1 = offline, >0 = ping in ms
    val isTesting: Boolean = false
) {
    val tgProxyUrl: String
        get() {
            // Convert standard https://t.me/proxy?server=... to tg://proxy?server=... for direct telegram launch
            return if (rawUrl.startsWith("https://t.me/proxy")) {
                rawUrl.replace("https://t.me/proxy", "tg://proxy")
            } else {
                rawUrl
            }
        }

    val displayServer: String
        get() = server.trimEnd('.')

    companion object {
        fun parseFromLine(id: String, line: String): ProxyItem? {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return null
            if (!trimmed.contains("server=") || !trimmed.contains("port=")) return null

            val server = extractParam(trimmed, "server") ?: return null
            val portStr = extractParam(trimmed, "port") ?: return null
            val port = portStr.toIntOrNull() ?: return null
            val secret = extractParam(trimmed, "secret") ?: ""

            return ProxyItem(
                id = id,
                rawUrl = trimmed,
                server = server,
                port = port,
                secret = secret
            )
        }

        private fun extractParam(url: String, param: String): String? {
            val pattern = "[?&]$param=([^&]+)".toRegex()
            val match = pattern.find(url)
            return match?.groupValues?.get(1)?.let {
                try {
                    URLDecoder.decode(it, "UTF-8")
                } catch (e: Exception) {
                    it
                }
            }
        }
    }
}
