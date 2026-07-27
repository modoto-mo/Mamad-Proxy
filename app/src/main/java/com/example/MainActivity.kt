package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.lerp
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import com.example.ui.theme.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope


enum class AppTab {
    HOME,
    LOCAL,
    SETTINGS
}

class Locales(val lang: AppLanguage) {
    val appTitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Mamad Proxy " else " Mamad Proxy"
    
    val appSubtitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Telegram proxy speed and ping tester" else "تستر پینگ و سرعت پروکسی‌های تلگرام"
    
    val testAll: String
        get() = if (lang == AppLanguage.ENGLISH) "Test All Speeds" else "تست سرعت همه"
    
    val refreshList: String
        get() = if (lang == AppLanguage.ENGLISH) "Update List" else "بروزرسانی لیست"
    
    val autoSort: String
        get() = if (lang == AppLanguage.ENGLISH) "Auto-sort" else "مرتب‌سازی خودکار"
    
    val creator: String
        get() = if (lang == AppLanguage.ENGLISH) "Creator" else "سازنده"
    
    val channel: String
        get() = if (lang == AppLanguage.ENGLISH) "Channel" else "کانال"
    
    val searchPlaceholder: String
        get() = if (lang == AppLanguage.ENGLISH) "Search server or port..." else "جستجوی سرور یا پورت..."
    
    val filterAll: String
        get() = if (lang == AppLanguage.ENGLISH) "All" else "همه"
    
    val filterOnline: String
        get() = if (lang == AppLanguage.ENGLISH) "Online" else "آنلاین"
    
    val filterFast: String
        get() = if (lang == AppLanguage.ENGLISH) "Fastest" else "سریع‌ترین‌ها"
    
    val foundProxies: String
        get() = if (lang == AppLanguage.ENGLISH) "Found proxies" else "پروکسی‌های پیدا شده"
    
    val autoSortActive: String
        get() = if (lang == AppLanguage.ENGLISH) "Auto-sort is active" else "مرتب‌سازی خودکار فعال است"
    
    val directConnect: String
        get() = if (lang == AppLanguage.ENGLISH) "Direct Connect" else "اتصال مستقیم"
    
    val copySuccess: String
        get() = if (lang == AppLanguage.ENGLISH) "Proxy link copied successfully" else "لینک پروکسی با موفقیت کپی شد"
    
    val shareTitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Share Proxy" else "اشتراک‌گذاری پروکسی"
    
    val noTelegram: String
        get() = if (lang == AppLanguage.ENGLISH) "No app found to open Telegram!" else "برنامه‌ای جهت باز کردن تلگرام یافت نشد!"
    
    val testing: String
        get() = if (lang == AppLanguage.ENGLISH) "Testing..." else "در حال تست..."
    
    val pingTest: String
        get() = if (lang == AppLanguage.ENGLISH) "Test Speed" else "تست سرعت"
    
    val offline: String
        get() = if (lang == AppLanguage.ENGLISH) "Offline" else "قطع"
    
    val loadingProxies: String
        get() = if (lang == AppLanguage.ENGLISH) "Fetching proxies from server..." else "در حال دریافت پروکسی‌ها از سرور..."
    
    val emptyList: String
        get() = if (lang == AppLanguage.ENGLISH) "Proxy list is empty!\nPlease click the update button." else "لیست پروکسی خالی است!\nلطفاً دکمه بروزرسانی را بزنید."
    
    val fetchAgain: String
        get() = if (lang == AppLanguage.ENGLISH) "Fetch Again" else "دریافت مجدد"
    
    val homeTab: String
        get() = if (lang == AppLanguage.ENGLISH) "Home" else "خانه"
    
    val localTab: String
        get() = if (lang == AppLanguage.ENGLISH) "Local" else "لوکال"
    
    val settingsTab: String
        get() = if (lang == AppLanguage.ENGLISH) "Settings" else "تنظیمات"
    
    val themeSetting: String
        get() = if (lang == AppLanguage.ENGLISH) "App Theme" else "پوسته برنامه"
    
    val themeDark: String
        get() = if (lang == AppLanguage.ENGLISH) "Dark Theme" else "پوسته تاریک"
    
    val themeLight: String
        get() = if (lang == AppLanguage.ENGLISH) "Light Theme" else "پوسته روشن"
    
    val langSetting: String
        get() = if (lang == AppLanguage.ENGLISH) "Language" else "زبان"
    
    val appearance: String
        get() = if (lang == AppLanguage.ENGLISH) "Appearance" else "ظاهر"
    
    val general: String
        get() = if (lang == AppLanguage.ENGLISH) "General" else "عمومی"
    
    val aboutTitle: String
        get() = if (lang == AppLanguage.ENGLISH) "About" else "درباره"
    
    val settingsTitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Settings" else "تنظیمات"

    val savedProxiesTitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Saved Proxies" else "پروکسی‌های ذخیره شده"

    val noSavedProxies: String
        get() = if (lang == AppLanguage.ENGLISH) "You haven't saved any proxies yet." else "هنوز هیچ پروکسی ذخیره نکرده‌اید."

    val testSavedSpeeds: String
        get() = if (lang == AppLanguage.ENGLISH) "Test Saved Speeds" else "تست سرعت ذخیره‌شده‌ها"

    val proxyTimeoutSetting: String
        get() = if (lang == AppLanguage.ENGLISH) "Proxy Timeout (ms)" else "تایم‌اوت پروکسی (میلی‌ثانیه)"

    val proxyTimeoutSubtitle: String
        get() = if (lang == AppLanguage.ENGLISH) "Maximum wait time for proxy response (Default: 700 ms)" else "حداکثر زمان انتظار پاسخ پروکسی (پیش‌فرض: ۷۰۰ میلی‌ثانیه)"

    val networkSettings: String
        get() = if (lang == AppLanguage.ENGLISH) "Network Settings" else "تنظیمات شبکه"
}

// ==========================================
// LIQUID GLASS COMPONENTS AND MODIFIERS
// ==========================================

@Composable
fun LiquidGlassBackground(isDarkMode: Boolean, content: @Composable BoxScope.() -> Unit) {
    val gradientColors = if (isDarkMode) {
        listOf(
            Color(0xFF0F1115), // Deep space black
            Color(0xFF161922), // Metallic midnight blue
            Color(0xFF201833), // Obsidian purple
            Color(0xFF0F1115)  // Back to space black
        )
    } else {
        listOf(
            Color(0xFFF0F4FF), // Warm pastel blue-sky
            Color(0xFFE5ECFF), // Silky lavender light
            Color(0xFFFFF0F5), // Light lavender pink glow
            Color(0xFFF0F4FF)  // Back to blue-sky
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = gradientColors
                )
            )
            .drawBehind {
                val primaryColor = if (isDarkMode) Color(0xFF5E35B1) else Color(0x33A1C4FD)
                val secondaryColor = if (isDarkMode) Color(0xFF00ACC1) else Color(0x33C2E9FB)
                
                // Top right glowing blob
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.15f),
                        radius = size.width * 0.7f
                    ),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.85f, size.height * 0.15f)
                )

                // Bottom left glowing blob
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(secondaryColor.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(size.width * 0.15f, size.height * 0.8f),
                        radius = size.width * 0.7f
                    ),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.15f, size.height * 0.8f)
                )
            }
    ) {
        content()
    }
}

fun Modifier.liquidGlassBorder(
    isDarkMode: Boolean,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(24.dp),
    borderAlpha: Float = 1.0f
): Modifier = this.border(
    border = BorderStroke(
        width = 1.2.dp,
        brush = Brush.linearGradient(
            colors = if (isDarkMode) {
                listOf(
                    Color.White.copy(alpha = 0.28f * borderAlpha), // Crisp highlights at top-left
                    Color.White.copy(alpha = 0.03f * borderAlpha),
                    Color(0xFFD0BCFF).copy(alpha = 0.12f * borderAlpha), // Gentle glowing color reflection
                    Color.White.copy(alpha = 0.05f * borderAlpha)
                )
            } else {
                listOf(
                    Color.White.copy(alpha = 0.7f * borderAlpha), // High gloss highlight
                    Color.White.copy(alpha = 0.2f * borderAlpha),
                    Color(0xFF6750A4).copy(alpha = 0.15f * borderAlpha),
                    Color.White.copy(alpha = 0.3f * borderAlpha)
                )
            }
        )
    ),
    shape = shape
)

@Composable
fun GlassMorphicCard(
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(24.dp),
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val bgColors = if (isDarkMode) {
        listOf(
            Color(0x25FFFFFF), // Top highlight frosting
            Color(0x12FFFFFF)  // Bottom deeper space transparency
        )
    } else {
        listOf(
            Color(0x1AFFFFFF), // Highly transparent light-mode highlight (10% opacity)
            Color(0x0AFFFFFF)  // Deeper transparency (4% opacity)
        )
    }

    Box(
        modifier = modifier
            .background(Brush.verticalGradient(bgColors), shape)
            .liquidGlassBorder(isDarkMode, shape)
            .padding(padding)
    ) {
        Column {
            content()
        }
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: ProxyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSettings(this)
        handleFileIntent(intent)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val language by viewModel.language.collectAsState()
            var currentTab by remember { mutableStateOf(AppTab.HOME) }
            val locales = remember(language) { Locales(language) }

            MyApplicationTheme(darkTheme = isDarkMode) {
                LiquidGlassBackground(isDarkMode = isDarkMode) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("main_scaffold"),
                        containerColor = Color.Transparent,
                        bottomBar = {
                            FloatingGlassmorphicNavBar(
                                currentTab = currentTab,
                                onTabSelected = { currentTab = it },
                                locales = locales,
                                isDarkMode = isDarkMode
                            )
                        }
                    ) { innerPadding ->
                        val layoutDirection = if (language == AppLanguage.PERSIAN) LayoutDirection.Rtl else LayoutDirection.Ltr
                        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = innerPadding.calculateBottomPadding())
                            ) {
                                when (currentTab) {
                                    AppTab.HOME -> {
                                        ProxyDashboardScreen(
                                            viewModel = viewModel,
                                            isDarkMode = isDarkMode,
                                            language = language,
                                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                                        )
                                    }
                                    AppTab.LOCAL -> {
                                        LocalProxiesScreen(
                                            viewModel = viewModel,
                                            isDarkMode = isDarkMode,
                                            language = language,
                                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                                        )
                                    }
                                    AppTab.SETTINGS -> {
                                        ProxySettingsScreen(
                                            viewModel = viewModel,
                                            isDarkMode = isDarkMode,
                                            language = language,
                                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleFileIntent(intent)
    }

    private fun handleFileIntent(intent: Intent?) {
        if (intent == null) return
        val action = intent.action
        val uri: Uri? = when (action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
            else -> null
        }
        uri?.let { fileUri ->
            try {
                contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    val text = inputStream.bufferedReader().use { it.readText() }
                    if (text.isNotBlank()) {
                        val fileNameWithoutExt = getFileNameFromUri(this, fileUri)
                        viewModel.createSubscription(fileNameWithoutExt, text) { _, count ->
                            val msg = if (viewModel.language.value == AppLanguage.ENGLISH)
                                "Created sub '$fileNameWithoutExt' ($count proxies)"
                            else
                                "ساب جدید «$fileNameWithoutExt» با $count پروکسی ایجاد شد"
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var fileName = ""
        if (uri.scheme == "content") {
            try {
                context.contentResolver.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                        if (nameIndex != -1) {
                            fileName = cursor.getString(nameIndex) ?: ""
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (fileName.isBlank()) {
            fileName = uri.lastPathSegment ?: ""
        }
        val cleanName = if (fileName.contains(".")) {
            fileName.substringBeforeLast(".")
        } else {
            fileName
        }
        return if (cleanName.isBlank()) "Imported Sub" else cleanName
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchProxies()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProxyDashboardScreen(
    viewModel: ProxyViewModel,
    isDarkMode: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locales = remember(language) { Locales(language) }
    val proxies by viewModel.proxies.collectAsState()
    val savedProxies by viewModel.savedProxies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isAutoSortEnabled by viewModel.isAutoSortEnabled.collectAsState()
    val isTestingAll by viewModel.isTestingAll.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterType by viewModel.filterType.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    // Resolve Dynamic Color values based on theme state
    val backgroundColor = if (isDarkMode) ElegantDarkBackground else MaterialTheme.colorScheme.background
    val surfaceColor = if (isDarkMode) ElegantDarkSurface else MaterialTheme.colorScheme.surface
    val cardBgColor = if (isDarkMode) ElegantItemCardBg else MaterialTheme.colorScheme.surfaceVariant
    val textPrimaryColor = if (isDarkMode) ElegantTextPrimary else MaterialTheme.colorScheme.onBackground
    val textSecondaryColor = if (isDarkMode) ElegantTextSecondary else MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = if (isDarkMode) ElegantPrimary else MaterialTheme.colorScheme.primary
    val onPrimaryColor = if (isDarkMode) ElegantOnPrimary else Color.White
    val borderCol = if (isDarkMode) ElegantSurfaceBorder else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    // Filters and search logic
    val filteredProxies = remember(proxies, searchQuery, filterType) {
        proxies.filter { proxy ->
            val matchesSearch = proxy.server.contains(searchQuery, ignoreCase = true) ||
                    proxy.port.toString().contains(searchQuery)
            
            val matchesFilter = when (filterType) {
                "ONLINE" -> proxy.pingMs != null && proxy.pingMs > 0
                "FAST" -> proxy.pingMs != null && proxy.pingMs in 1..150
                else -> true
            }
            
            matchesSearch && matchesFilter
        }
    }

    // Custom Built high quality vector Icons
    val copyIcon = remember {
        ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(16f, 1f)
                lineTo(4f, 1f)
                quadTo(3f, 1f, 3f, 2f)
                lineTo(3f, 14f)
                horizontalLineTo(5f)
                lineTo(5f, 3f)
                horizontalLineTo(16f)
                close()
                moveTo(19f, 5f)
                lineTo(8f, 5f)
                quadTo(7f, 5f, 7f, 6f)
                lineTo(7f, 20f)
                quadTo(7f, 21f, 8f, 21f)
                lineTo(19f, 21f)
                quadTo(20f, 21f, 20f, 20f)
                lineTo(20f, 6f)
                quadTo(20f, 5f, 19f, 5f)
                close()
                moveTo(18f, 19f)
                horizontalLineTo(9f)
                lineTo(9f, 7f)
                horizontalLineTo(18f)
                close()
            }
        }.build()
    }

    val speedIcon = remember {
        ImageVector.Builder(
            name = "Speed",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(12f, 2f)
                arcTo(10f, 10f, 0f, false, false, 2f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 22f)
                arcTo(10f, 10f, 0f, false, false, 22f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 2f)
                close()
                moveTo(12f, 4f)
                arcTo(8f, 8f, 0f, false, true, 18.5f, 7.5f)
                lineTo(16.5f, 9.5f)
                arcTo(5f, 5f, 0f, false, false, 12f, 7f)
                arcTo(5f, 5f, 0f, false, false, 7f, 12f)
                arcTo(5f, 5f, 0f, false, false, 12f, 17f)
                arcTo(5f, 5f, 0f, false, false, 15.5f, 15.5f)
                lineTo(17.5f, 17.5f)
                arcTo(8f, 8f, 0f, false, true, 12f, 20f)
                arcTo(8f, 8f, 0f, false, true, 4f, 12f)
                arcTo(8f, 8f, 0f, false, true, 12f, 4f)
                close()
                moveTo(12f, 9f)
                arcTo(3f, 3f, 0f, false, false, 9f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 15f)
                arcTo(3f, 3f, 0f, false, false, 15f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 9f)
                close()
                moveTo(13f, 11f)
                lineTo(16f, 8f)
                lineTo(17f, 9f)
                lineTo(14f, 12f)
                close()
            }
        }.build()
    }

    val paperPlaneIcon = remember {
        ImageVector.Builder(
            name = "PaperPlane",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(2f, 21f)
                lineTo(23f, 12f)
                lineTo(2f, 3f)
                lineTo(2f, 10f)
                lineTo(17f, 12f)
                lineTo(2f, 14f)
                close()
            }
        }.build()
    }

    val lightningIcon = remember {
        ImageVector.Builder(
            name = "Lightning",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(7f, 2f)
                lineTo(17f, 2f)
                lineTo(12f, 11f)
                lineTo(19f, 11f)
                lineTo(9f, 22f)
                lineTo(12f, 13f)
                close()
            }
        }.build()
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = { viewModel.fetchProxies() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentPadding = PaddingValues(bottom = 100.dp) // generous padding for floating bottom bar
        ) {
            item {
                // بنر اصلی - بدون پس‌زمینه بلور
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1E2129) else Color(0xFFEBECEF)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // فقط خود عکس اصلی - با Crop تا کل فضا رو پر کنه
                        Image(
                            painter = painterResource(id = R.drawable.ic_hero_banner),
                            contentDescription = "Branding Header Logo",
                            modifier = Modifier
                                .fillMaxSize(), // پدینگ رو بردار! (میخوایم کل فضا رو پر کنه)
                            contentScale = ContentScale.Crop // این رو از Fit به Crop تغییر بده
                        )
                    }
                }
            }

            item {
                // Brand Information and Network Stats (positioned outside to ensure banner content is never overlapped or hidden)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = locales.appTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = if (isDarkMode) Color.White else Color(0xFF1A1C1E),
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = locales.appSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Connected/Total stats badge
                    Surface(
                        color = if (isDarkMode) Color(0x1AFFFFFF) else Color(0x0A000000),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.liquidGlassBorder(isDarkMode, RoundedCornerShape(14.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val onlineCount = proxies.count { it.pingMs != null && it.pingMs > 0 }
                            Icon(
                                imageVector = lightningIcon,
                                contentDescription = "Online Status",
                                tint = if (isDarkMode) ElegantGreen else Color(0xFF2E7D32),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$onlineCount / ${proxies.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isDarkMode) Color.White else Color(0xFF1A1C1E),
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            item {
                // Global Action Controls wrapped in GlassMorphicCard
                GlassMorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    isDarkMode = isDarkMode,
                    padding = PaddingValues(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Test All Speeds button with custom 3D glossy top edge highlight
                            val testAllEnabled = !isTestingAll && proxies.isNotEmpty()
                            val buttonBg = if (isDarkMode) {
                                Brush.verticalGradient(listOf(Color(0xFF8E24AA), Color(0xFF5E35B1)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0xFFAB47BC), Color(0xFF7E57C2)))
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (testAllEnabled) buttonBg else Brush.verticalGradient(listOf(Color.Gray.copy(alpha=0.2f), Color.Gray.copy(alpha=0.2f))))
                                    .clickable(enabled = testAllEnabled) { viewModel.testAllProxies() }
                                    .drawBehind {
                                        // Top glossy edge highlight
                                        val highlightColor = Color.White.copy(alpha = 0.35f)
                                        drawLine(
                                            color = highlightColor,
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, 0f),
                                            strokeWidth = 2.dp.toPx()
                                        )
                                    }
                                    .testTag("test_all_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isTestingAll) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = onPrimaryColor,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(speedIcon, contentDescription = "Speed Test All", modifier = Modifier.size(18.dp), tint = onPrimaryColor)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(locales.testAll, fontSize = 12.sp, fontWeight = FontWeight.Black, color = onPrimaryColor)
                                    }
                                }
                            }

                            // Reload/Fetch List styled as a gorgeous liquid glass capsule
                            val secondaryBtnBg = if (isDarkMode) {
                                Brush.verticalGradient(listOf(Color(0x2EFFFFFF), Color(0x11FFFFFF)))
                            } else {
                                Brush.verticalGradient(listOf(Color(0x1AFFFFFF), Color(0x0AFFFFFF)))
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(secondaryBtnBg)
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(14.dp))
                                    .clickable(enabled = !isLoading) { viewModel.fetchProxies() }
                                    .drawBehind {
                                        // Top glossy reflection edge
                                        drawLine(
                                            color = Color.White.copy(alpha = 0.25f),
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, 0f),
                                            strokeWidth = 1.5.dp.toPx()
                                        )
                                    }
                                    .testTag("refresh_list_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val rotationAnim = remember { Animatable(0f) }
                                    LaunchedEffect(isLoading) {
                                        if (isLoading) {
                                            rotationAnim.animateTo(
                                                targetValue = 360f,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(1200, easing = LinearEasing),
                                                    repeatMode = RepeatMode.Restart
                                                )
                                            )
                                        } else {
                                            rotationAnim.snapTo(0f)
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        modifier = Modifier
                                            .size(18.dp)
                                            .graphicsLayer(rotationZ = rotationAnim.value),
                                        tint = if (isDarkMode) Color.White else Color(0xFF6750A4)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(locales.refreshList, fontSize = 12.sp, fontWeight = FontWeight.Black, color = if (isDarkMode) Color.White else Color(0xFF6750A4))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Auto Sort Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.toggleAutoSort() }
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                if (isAutoSortEnabled) {
                                    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                    val alpha by infiniteTransition.animateFloat(
                                        initialValue = 0.3f,
                                        targetValue = 1.0f,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(800, easing = LinearEasing),
                                            repeatMode = RepeatMode.Reverse
                                        ),
                                        label = "alpha"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .graphicsLayer(alpha = alpha)
                                            .background(ElegantGreen, CircleShape)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(ElegantGray, CircleShape)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = locales.autoSort,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isAutoSortEnabled) textPrimaryColor else textSecondaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Switch(
                                checked = isAutoSortEnabled,
                                onCheckedChange = { viewModel.toggleAutoSort() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = primaryColor,
                                    uncheckedThumbColor = Color.Gray,
                                    uncheckedTrackColor = if (isDarkMode) Color(0x3DFFFFFF) else Color(0x1F000000)
                                ),
                                modifier = Modifier.scale(0.85f)
                            )
                        }
                    }
                }
            }

            item {
                // Creator & Channel Premium Card wrapped in GlassMorphicCard
                GlassMorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    isDarkMode = isDarkMode,
                    padding = PaddingValues(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Creator Column
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x14FFFFFF), RoundedCornerShape(16.dp))
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(16.dp))
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=frzmmd"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/frzmmd"))
                                                context.startActivity(intent)
                                            } catch (e2: Exception) {
                                                Toast.makeText(context, locales.noTelegram, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = locales.creator,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "❄️Frozen Mamad❄️",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDarkMode) Color(0xFFB6EEA9) else Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Black,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Requested Channel Column
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x14FFFFFF), RoundedCornerShape(16.dp))
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(16.dp))
                                    .clickable {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=mamad1vpn"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mamad1vpn"))
                                                context.startActivity(intent)
                                            } catch (e2: Exception) {
                                                Toast.makeText(context, locales.noTelegram, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = locales.channel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f),
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Mamad Config",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4),
                                    fontWeight = FontWeight.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Search Input Field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text(locales.searchPlaceholder, fontSize = 13.sp, color = if (isDarkMode) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f), fontWeight = FontWeight.Medium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .liquidGlassBorder(isDarkMode, RoundedCornerShape(16.dp))
                            .testTag("search_input"),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f))
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkMode) Color(0x1FFFFFFF) else Color(0x7AFFFFFF),
                            unfocusedContainerColor = if (isDarkMode) Color(0x12FFFFFF) else Color(0x40FFFFFF),
                            focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkMode) Color.White else Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                        singleLine = true
                    )
                }
            }

            item {
                // Filters Tabs Row with beautiful responsive design (Segmented control inside GlassMorphicCard)
                GlassMorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    isDarkMode = isDarkMode,
                    padding = PaddingValues(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        FilterTabButton(
                            text = "${locales.filterAll} (${proxies.size})",
                            selected = filterType == "ALL",
                            onClick = { viewModel.setFilterType("ALL") },
                            isDarkMode = isDarkMode,
                            primaryColor = primaryColor,
                            surfaceColor = surfaceColor,
                            textSecondaryColor = textSecondaryColor,
                            modifier = Modifier.weight(1.5f)
                        )
                        FilterTabButton(
                            text = locales.filterOnline,
                            selected = filterType == "ONLINE",
                            onClick = { viewModel.setFilterType("ONLINE") },
                            isDarkMode = isDarkMode,
                            primaryColor = primaryColor,
                            surfaceColor = surfaceColor,
                            textSecondaryColor = textSecondaryColor,
                            modifier = Modifier.weight(1.2f)
                        )
                        FilterTabButton(
                            text = locales.filterFast,
                            selected = filterType == "FAST",
                            onClick = { viewModel.setFilterType("FAST") },
                            isDarkMode = isDarkMode,
                            primaryColor = primaryColor,
                            surfaceColor = surfaceColor,
                            textSecondaryColor = textSecondaryColor,
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                }
            }

            item {
                // List Header Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${locales.foundProxies} (${filteredProxies.size})",
                        style = MaterialTheme.typography.labelLarge,
                        color = textSecondaryColor,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (isAutoSortEnabled && proxies.isNotEmpty()) {
                        Text(
                            text = locales.autoSortActive,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElegantGreen,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Proxies Lazy Loading list items
            if (filteredProxies.isEmpty()) {
                item {
                    EmptyStateView(
                        isLoading = isLoading,
                        locales = locales,
                        isDarkMode = isDarkMode,
                        onRefresh = { viewModel.fetchProxies() }
                    )
                }
            } else {
                items(
                    items = filteredProxies,
                    key = { it.id }
                ) { proxy ->
                    ProxyItemCard(
                        proxy = proxy,
                        locales = locales,
                        isDarkMode = isDarkMode,
                        isSaved = savedProxies.any { it.rawUrl == proxy.rawUrl },
                        onToggleSave = { viewModel.toggleSaveProxy(proxy) },
                        onTestPing = { viewModel.testProxyPing(proxy.id) },
                        onConnect = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proxy.tgProxyUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                try {
                                    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(proxy.rawUrl))
                                    context.startActivity(fallbackIntent)
                                } catch (e2: Exception) {
                                    Toast.makeText(context, locales.noTelegram, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        onCopyLink = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Telegram Proxy", proxy.rawUrl)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, locales.copySuccess, Toast.LENGTH_SHORT).show()
                        },
                        copyIcon = copyIcon,
                        speedIcon = speedIcon,
                        paperPlaneIcon = paperPlaneIcon,
                        lightningIcon = lightningIcon
                    )
                }
            }
        }
    }
}

@Composable
fun FilterTabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    isDarkMode: Boolean,
    primaryColor: Color,
    surfaceColor: Color,
    textSecondaryColor: Color,
    modifier: Modifier = Modifier
) {
    val bgColor = if (selected) {
        if (isDarkMode) Color(0x2EFFFFFF) else Color(0x33FFFFFF) // 18% alpha white / 20% alpha white
    } else {
        Color.Transparent
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .then(
                if (selected) {
                    Modifier.liquidGlassBorder(
                        isDarkMode = isDarkMode,
                        shape = RoundedCornerShape(10.dp),
                        borderAlpha = 0.4f
                    )
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick)
            .drawBehind {
                if (selected) {
                    // Dynamic bottom active glow line
                    val glowCol = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4)
                    drawRoundRect(
                        color = glowCol.copy(alpha = 0.4f),
                        topLeft = Offset(8.dp.toPx(), size.height - 3.dp.toPx()),
                        size = Size(size.width - 16.dp.toPx(), 2.dp.toPx()),
                        cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                if (isDarkMode) Color.White else Color(0xFF1A1C1E)
            } else {
                if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
            },
            fontWeight = if (selected) FontWeight.Black else FontWeight.Bold
        )
    }
}

@Composable
fun ProxyItemCard(
    proxy: ProxyItem,
    locales: Locales,
    isDarkMode: Boolean,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onTestPing: () -> Unit,
    onConnect: () -> Unit,
    onCopyLink: () -> Unit,
    copyIcon: ImageVector,
    speedIcon: ImageVector,
    paperPlaneIcon: ImageVector,
    lightningIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    val textPrimaryColor = if (isDarkMode) Color.White else Color(0xFF1A1C1E)
    val textSecondaryColor = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
    val primaryColor = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4)

    var showQrDialog by remember { mutableStateOf(false) }

    val qrIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "QrCode",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(3f, 3f); horizontalLineTo(11f); verticalLineTo(11f); horizontalLineTo(3f); close()
                moveTo(5f, 5f); verticalLineTo(9f); horizontalLineTo(9f); verticalLineTo(5f); close()
                moveTo(13f, 3f); horizontalLineTo(21f); verticalLineTo(11f); horizontalLineTo(13f); close()
                moveTo(15f, 5f); verticalLineTo(9f); horizontalLineTo(19f); verticalLineTo(5f); close()
                moveTo(3f, 13f); horizontalLineTo(11f); verticalLineTo(21f); horizontalLineTo(3f); close()
                moveTo(5f, 15f); verticalLineTo(19f); horizontalLineTo(9f); verticalLineTo(15f); close()
                moveTo(13f, 13f); horizontalLineTo(16f); verticalLineTo(16f); horizontalLineTo(13f); close()
                moveTo(18f, 13f); horizontalLineTo(21f); verticalLineTo(16f); horizontalLineTo(18f); close()
                moveTo(13f, 18f); horizontalLineTo(16f); verticalLineTo(21f); horizontalLineTo(13f); close()
                moveTo(18f, 18f); horizontalLineTo(21f); verticalLineTo(21f); horizontalLineTo(18f); close()
                moveTo(16f, 16f); horizontalLineTo(18f); verticalLineTo(18f); horizontalLineTo(16f); close()
            }
        }.build()
    }

    if (showQrDialog) {
        QrCodeModalDialog(
            title = proxy.displayServer,
            subtitle = "Port: ${proxy.port}",
            qrContent = proxy.rawUrl,
            isDarkMode = isDarkMode,
            language = locales.lang,
            onDismiss = { showQrDialog = false }
        )
    }

    GlassMorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("proxy_card_${proxy.port}"),
        isDarkMode = isDarkMode,
        padding = PaddingValues(14.dp)
    ) {
        Column {
            // Server details and Ping Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Glass styled circular server icon with top specular reflection
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = if (isDarkMode) {
                                        listOf(Color(0x3DFFFFFF), Color(0x11FFFFFF))
                                    } else {
                                        listOf(Color(0x80FFFFFF), Color(0x40FFFFFF))
                                    }
                                ),
                                CircleShape
                            )
                            .liquidGlassBorder(isDarkMode, CircleShape, borderAlpha = 0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = paperPlaneIcon,
                            contentDescription = "Server",
                            tint = primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = proxy.displayServer,
                            style = MaterialTheme.typography.bodyLarge,
                            color = textPrimaryColor,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Port: ${proxy.port}",
                            style = MaterialTheme.typography.bodySmall,
                            color = textSecondaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Ping Status Badge (ultra polished glass capsule)
                PingBadge(
                    pingMs = proxy.pingMs,
                    isTesting = proxy.isTesting,
                    locales = locales,
                    isDarkMode = isDarkMode,
                    onTestClick = onTestPing,
                    speedIcon = speedIcon
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Secret display
            if (proxy.secret.isNotEmpty()) {
                Surface(
                    color = if (isDarkMode) Color(0x0CFFFFFF) else Color(0x2EFFFFFF),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .liquidGlassBorder(isDarkMode, RoundedCornerShape(10.dp), borderAlpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Secret: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = primaryColor,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = proxy.secret,
                            style = MaterialTheme.typography.bodySmall,
                            color = textPrimaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Main connect button
                Button(
                    onClick = onConnect,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(42.dp)
                        .testTag("connect_button_${proxy.port}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val connectBg = if (isDarkMode) {
                        Brush.verticalGradient(listOf(Color(0xFF388E3C), Color(0xFF1B5E20)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(connectBg)
                            .drawBehind {
                                // Specs reflection line
                                drawLine(
                                    color = Color.White.copy(alpha = 0.35f),
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = 1.5.dp.toPx()
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = lightningIcon,
                                contentDescription = "Connect",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(locales.directConnect, fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                // Copy Link button with Glass appearance
                IconButton(
                    onClick = onCopyLink,
                    modifier = Modifier
                        .size(42.dp)
                        .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x54FFFFFF), RoundedCornerShape(12.dp))
                        .liquidGlassBorder(isDarkMode, RoundedCornerShape(12.dp), borderAlpha = 0.2f)
                        .testTag("copy_button_${proxy.port}")
                ) {
                    Icon(
                        imageVector = copyIcon,
                        contentDescription = "Copy Link",
                        tint = textPrimaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Save button with Glass appearance
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .size(42.dp)
                        .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x54FFFFFF), RoundedCornerShape(12.dp))
                        .liquidGlassBorder(isDarkMode, RoundedCornerShape(12.dp), borderAlpha = 0.2f)
                        .testTag("save_button_${proxy.port}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Save Proxy",
                        tint = if (isSaved) Color(0xFFFFA000) else textPrimaryColor.copy(alpha = 0.35f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                val localContext = LocalContext.current
                // QR Code button
                IconButton(
                    onClick = { showQrDialog = true },
                    modifier = Modifier
                        .size(42.dp)
                        .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x54FFFFFF), RoundedCornerShape(12.dp))
                        .liquidGlassBorder(isDarkMode, RoundedCornerShape(12.dp), borderAlpha = 0.2f)
                        .testTag("qr_button_${proxy.port}")
                ) {
                    Icon(
                        imageVector = qrIcon,
                        contentDescription = "QR Code",
                        tint = textPrimaryColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Share button with Glass appearance
                IconButton(
                    onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, proxy.rawUrl)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, locales.shareTitle)
                        localContext.startActivity(shareIntent)
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x54FFFFFF), RoundedCornerShape(12.dp))
                        .liquidGlassBorder(isDarkMode, RoundedCornerShape(12.dp), borderAlpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = textPrimaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PingBadge(
    pingMs: Long?,
    isTesting: Boolean,
    locales: Locales,
    isDarkMode: Boolean,
    onTestClick: () -> Unit,
    speedIcon: ImageVector
) {
    val primaryColor = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val testingRot by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "testingRot"
    )

    val bgColor = when {
        isTesting -> primaryColor.copy(alpha = 0.15f)
        pingMs == null -> Color.Gray.copy(alpha = 0.12f)
        pingMs == -1L -> ElegantRed.copy(alpha = 0.15f)
        pingMs < 150 -> ElegantGreen.copy(alpha = 0.15f)
        pingMs < 350 -> ElegantAmber.copy(alpha = 0.15f)
        pingMs < 600 -> ElegantOrange.copy(alpha = 0.15f)
        else -> ElegantRed.copy(alpha = 0.15f)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .liquidGlassBorder(
                isDarkMode = isDarkMode,
                shape = RoundedCornerShape(12.dp),
                borderAlpha = 0.25f
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onTestClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isTesting) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = locales.testing,
                    tint = primaryColor,
                    modifier = Modifier
                        .size(14.dp)
                        .graphicsLayer(rotationZ = testingRot)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = locales.testing,
                    color = primaryColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            } else {
                val tintColor = when {
                    pingMs == null -> Color.Gray
                    pingMs == -1L -> ElegantRed
                    pingMs < 150 -> ElegantGreen
                    pingMs < 350 -> ElegantAmber
                    pingMs < 600 -> ElegantOrange
                    else -> ElegantRed
                }

                Icon(
                    imageVector = speedIcon,
                    contentDescription = "Ping Result",
                    tint = tintColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = when {
                        pingMs == null -> locales.pingTest
                        pingMs == -1L -> locales.offline
                        else -> "$pingMs ms"
                    },
                    color = tintColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun EmptyStateView(
    isLoading: Boolean,
    locales: Locales,
    isDarkMode: Boolean,
    onRefresh: () -> Unit
) {
    val primaryColor = if (isDarkMode) Color(0xFFD0BCFF) else Color(0xFF6750A4)
    val textPrimaryColor = if (isDarkMode) Color.White else Color(0xFF1A1C1E)
    val textSecondaryColor = if (isDarkMode) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = primaryColor,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = locales.loadingProxies,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textSecondaryColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "No Proxies",
                    tint = primaryColor,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = locales.emptyList,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textPrimaryColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRefresh,
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(locales.fetchAgain, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun ProxySettingsScreen(
    viewModel: ProxyViewModel,
    isDarkMode: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locales = remember(language) { Locales(language) }
    
    val backgroundColor = if (isDarkMode) ElegantDarkBackground else MaterialTheme.colorScheme.background
    val surfaceColor = if (isDarkMode) ElegantDarkSurface else MaterialTheme.colorScheme.surface
    val cardBgColor = if (isDarkMode) ElegantItemCardBg else MaterialTheme.colorScheme.surface
    val textPrimaryColor = if (isDarkMode) ElegantTextPrimary else MaterialTheme.colorScheme.onBackground
    val textSecondaryColor = if (isDarkMode) ElegantTextSecondary else MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = if (isDarkMode) ElegantPrimary else MaterialTheme.colorScheme.primary
    val borderCol = if (isDarkMode) ElegantSurfaceBorder else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    val moonColor = if (isDarkMode) ElegantPrimary else MaterialTheme.colorScheme.primary
    val globeColor = if (isDarkMode) ElegantPrimary else MaterialTheme.colorScheme.primary

    // Handcrafted Custom Moon/Sun and Language Icons
    val themeIcon = remember(isDarkMode, moonColor) {
        ImageVector.Builder(
            name = "ThemeMoonSun",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(moonColor)) {
                moveTo(12f, 3f)
                arcTo(9f, 9f, 0f, true, false, 21f, 12f)
                arcTo(9f, 9f, 0f, false, true, 12f, 3f)
                close()
            }
        }.build()
    }

    val languageIcon = remember(isDarkMode, globeColor) {
        ImageVector.Builder(
            name = "LanguageGlobe",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(globeColor)) {
                moveTo(11.99f, 2f)
                arcTo(10f, 10f, 0f, false, false, 2f, 12f)
                arcTo(10f, 10f, 0f, false, false, 11.99f, 22f)
                arcTo(10f, 10f, 0f, false, false, 22f, 12f)
                arcTo(10f, 10f, 0f, false, false, 11.99f, 2f)
                close()
                moveTo(18.91f, 8f)
                horizontalLineTo(15.78f)
                arcTo(16.15f, 16.15f, 0f, false, false, 14.36f, 4.25f)
                arcTo(8f, 8f, 0f, false, true, 18.91f, 8f)
                close()
                moveTo(12f, 4.04f)
                arcTo(14f, 14f, 0f, false, true, 13.73f, 8f)
                horizontalLineTo(10.27f)
                arcTo(14f, 14f, 0f, false, true, 12f, 4.04f)
                close()
            }
        }.build()
    }

    val copyIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(16f, 1f)
                lineTo(4f, 1f)
                quadTo(3f, 1f, 3f, 2f)
                lineTo(3f, 14f)
                horizontalLineTo(5f)
                lineTo(5f, 3f)
                horizontalLineTo(16f)
                close()
                moveTo(19f, 5f)
                lineTo(8f, 5f)
                quadTo(7f, 5f, 7f, 6f)
                lineTo(7f, 20f)
                quadTo(7f, 21f, 8f, 21f)
                lineTo(19f, 21f)
                quadTo(20f, 21f, 20f, 20f)
                lineTo(20f, 6f)
                quadTo(20f, 5f, 19f, 5f)
                close()
                moveTo(18f, 19f)
                horizontalLineTo(9f)
                lineTo(9f, 7f)
                horizontalLineTo(18f)
                close()
            }
        }.build()
    }

    val speedIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "Speed",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(12f, 2f)
                arcTo(10f, 10f, 0f, false, false, 2f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 22f)
                arcTo(10f, 10f, 0f, false, false, 22f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 2f)
                close()
                moveTo(12f, 4f)
                arcTo(8f, 8f, 0f, false, true, 18.5f, 7.5f)
                lineTo(16.5f, 9.5f)
                arcTo(5f, 5f, 0f, false, false, 12f, 7f)
                arcTo(5f, 5f, 0f, false, false, 7f, 12f)
                arcTo(5f, 5f, 0f, false, false, 12f, 17f)
                arcTo(5f, 5f, 0f, false, false, 15.5f, 15.5f)
                lineTo(17.5f, 17.5f)
                arcTo(8f, 8f, 0f, false, true, 12f, 20f)
                arcTo(8f, 8f, 0f, false, true, 4f, 12f)
                arcTo(8f, 8f, 0f, false, true, 12f, 4f)
                close()
                moveTo(12f, 9f)
                arcTo(3f, 3f, 0f, false, false, 9f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 15f)
                arcTo(3f, 3f, 0f, false, false, 15f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 9f)
                close()
                moveTo(13f, 11f)
                lineTo(16f, 8f)
                lineTo(17f, 9f)
                lineTo(14f, 12f)
                close()
            }
        }.build()
    }

    val paperPlaneIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "PaperPlane",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(2f, 21f)
                lineTo(23f, 12f)
                lineTo(2f, 3f)
                lineTo(2f, 10f)
                lineTo(17f, 12f)
                lineTo(2f, 14f)
                close()
            }
        }.build()
    }

    val lightningIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "Lightning",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(7f, 2f)
                lineTo(17f, 2f)
                lineTo(12f, 11f)
                lineTo(19f, 11f)
                lineTo(9f, 22f)
                lineTo(12f, 13f)
                close()
            }
        }.build()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp) // extra padding for bottom navigation capsule
    ) {
        // Settings Header
        item {
            Column(modifier = Modifier.padding(bottom = 20.dp, start = 4.dp)) {
                Text(
                    text = locales.settingsTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = textPrimaryColor,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Configure your experience" else "تنظیمات برنامه را سفارشی کنید",
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Theme preference group
        item {
            SettingsSectionHeader(title = locales.appearance, isDarkMode = isDarkMode, primaryColor = primaryColor)
            
            GlassMorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                isDarkMode = isDarkMode,
                padding = PaddingValues(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleDarkMode(context) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (isDarkMode) Color(0x26FFFFFF) else Color(0x3D6750A4), CircleShape)
                                .liquidGlassBorder(isDarkMode, CircleShape, borderAlpha = 0.25f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = themeIcon,
                                contentDescription = "Theme Icon",
                                tint = if (isDarkMode) Color.White else Color(0xFF6750A4),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = locales.themeSetting,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textPrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDarkMode) locales.themeDark else locales.themeLight,
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(context) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = if (isDarkMode) Color(0x3DFFFFFF) else Color(0x1F000000)
                        )
                    )
                }
            }
        }

        // Language toggle group
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsSectionHeader(title = locales.langSetting, isDarkMode = isDarkMode, primaryColor = primaryColor)
            
            GlassMorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                isDarkMode = isDarkMode,
                padding = PaddingValues(0.dp)
            ) {
                Column {
                    LanguageOptionRow(
                        languageOption = AppLanguage.PERSIAN,
                        selected = language == AppLanguage.PERSIAN,
                        onSelect = { viewModel.setLanguage(context, AppLanguage.PERSIAN) },
                        textPrimaryColor = textPrimaryColor,
                        textSecondaryColor = textSecondaryColor,
                        primaryColor = primaryColor
                    )
                    Divider(color = borderCol, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                    LanguageOptionRow(
                        languageOption = AppLanguage.ENGLISH,
                        selected = language == AppLanguage.ENGLISH,
                        onSelect = { viewModel.setLanguage(context, AppLanguage.ENGLISH) },
                        textPrimaryColor = textPrimaryColor,
                        textSecondaryColor = textSecondaryColor,
                        primaryColor = primaryColor
                    )
                }
            }
        }

        // Proxy Timeout configuration setting
        item {
            val currentTimeout by viewModel.proxyTimeoutMs.collectAsState()
            var textValue by remember(currentTimeout) { mutableStateOf(currentTimeout.toString()) }

            Spacer(modifier = Modifier.height(16.dp))
            SettingsSectionHeader(title = locales.networkSettings, isDarkMode = isDarkMode, primaryColor = primaryColor)
            
            GlassMorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                isDarkMode = isDarkMode,
                padding = PaddingValues(16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = locales.proxyTimeoutSetting,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textPrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = locales.proxyTimeoutSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = textValue,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.filter { it.isDigit() }
                                if (digitsOnly.length <= 5) {
                                    textValue = digitsOnly
                                    val parsed = digitsOnly.toIntOrNull()
                                    if (parsed != null && parsed in 100..10000) {
                                        viewModel.setProxyTimeoutMs(context, parsed)
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                val parsed = textValue.toIntOrNull() ?: 700
                                val clamped = parsed.coerceIn(100, 10000)
                                textValue = clamped.toString()
                                viewModel.setProxyTimeoutMs(context, clamped)
                            }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = borderCol,
                                focusedTextColor = textPrimaryColor,
                                unfocusedTextColor = textPrimaryColor
                            ),
                            suffix = {
                                Text(
                                    text = if (language == AppLanguage.ENGLISH) "ms" else "میلی‌ثانیه",
                                    fontSize = 11.sp,
                                    color = textSecondaryColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset Chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(500, 700, 1000, 1500, 2000).forEach { presetMs ->
                            val isSelected = currentTimeout == presetMs
                            val chipBg = if (isSelected) primaryColor.copy(alpha = 0.25f) else (if (isDarkMode) Color(0x1AFFFFFF) else Color(0x1F000000))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(chipBg)
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(8.dp), borderAlpha = if (isSelected) 0.6f else 0.15f)
                                    .clickable {
                                        viewModel.setProxyTimeoutMs(context, presetMs)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (presetMs == 700) "$presetMs *" else "$presetMs",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    color = if (isSelected) primaryColor else textPrimaryColor
                                )
                            }
                        }
                    }
                }
            }
        }

        // Requested Channel Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsSectionHeader(title = if (language == AppLanguage.ENGLISH) "Support Channel" else "کانال پشتیبانی", isDarkMode = isDarkMode, primaryColor = primaryColor)
            
            GlassMorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                isDarkMode = isDarkMode,
                padding = PaddingValues(16.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isDarkMode) Color(0x1AFFFFFF) else Color(0x3DFFFFFF), CircleShape)
                                .liquidGlassBorder(isDarkMode, CircleShape, borderAlpha = 0.25f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📢",
                                fontSize = 22.sp
                             )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Mamad Config",
                                style = MaterialTheme.typography.bodyLarge,
                                color = textPrimaryColor,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "@mamad1vpn",
                                style = MaterialTheme.typography.bodySmall,
                                color = primaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) 
                            "Get the latest high-speed proxies, VPN configs, and tech updates directly on our Telegram channel!" 
                            else "آخرین پروکسی‌های پرسرعت، کانفیگ‌های VPN و بروزرسانی‌های تکنولوژی را مستقیماً در کانال تلگرام ما دریافت کنید!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondaryColor,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=mamad1vpn"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mamad1vpn"))
                                    context.startActivity(intent)
                                } catch (e2: Exception) {
                                    Toast.makeText(context, locales.noTelegram, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        val joinBg = if (isDarkMode) {
                            Brush.verticalGradient(listOf(Color(0xFF8E24AA), Color(0xFF5E35B1)))
                        } else {
                            Brush.verticalGradient(listOf(Color(0xFFAB47BC), Color(0xFF7E57C2)))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(joinBg)
                                .drawBehind {
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.35f),
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (language == AppLanguage.ENGLISH) "Join Channel" else "عضویت در کانال",
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Saved Proxies Section
        item {
            val savedProxiesList by viewModel.savedProxiesList.collectAsState()
            val isTestingAllSaved by viewModel.isTestingAllSaved.collectAsState()

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SettingsSectionHeader(title = locales.savedProxiesTitle, isDarkMode = isDarkMode, primaryColor = primaryColor)
                
                if (savedProxiesList.isNotEmpty()) {
                    Button(
                        onClick = { viewModel.testAllSavedProxies() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = primaryColor
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .liquidGlassBorder(isDarkMode, RoundedCornerShape(8.dp), borderAlpha = 0.15f)
                            .background(if (isDarkMode) Color(0x0CFFFFFF) else Color(0x1F000000), RoundedCornerShape(8.dp)),
                        enabled = !isTestingAllSaved
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Test Saved Speeds",
                                modifier = Modifier.size(12.dp),
                                tint = primaryColor
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isTestingAllSaved) {
                                    if (language == AppLanguage.ENGLISH) "Testing..." else "در حال تست..."
                                } else {
                                    locales.testSavedSpeeds
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            if (savedProxiesList.isEmpty()) {
                GlassMorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    isDarkMode = isDarkMode,
                    padding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = locales.noSavedProxies,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondaryColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    )
                }
            }
        }

        val savedProxiesListLocal = viewModel.savedProxiesList.value
        items(
            items = viewModel.savedProxiesList.value,
            key = { "saved_" + it.id }
        ) { proxy ->
            val savedProxiesListState by viewModel.savedProxiesList.collectAsState()
            val activeProxy = savedProxiesListState.find { it.rawUrl == proxy.rawUrl } ?: proxy
            
            ProxyItemCard(
                proxy = activeProxy,
                locales = locales,
                isDarkMode = isDarkMode,
                isSaved = true,
                onToggleSave = { viewModel.toggleSaveProxy(activeProxy) },
                onTestPing = { viewModel.testSavedProxyPing(activeProxy.id) },
                onConnect = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeProxy.tgProxyUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        try {
                            val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(activeProxy.rawUrl))
                            context.startActivity(fallbackIntent)
                        } catch (e2: Exception) {
                            Toast.makeText(context, locales.noTelegram, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onCopyLink = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Telegram Proxy", activeProxy.rawUrl)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, locales.copySuccess, Toast.LENGTH_SHORT).show()
                },
                copyIcon = copyIcon,
                speedIcon = speedIcon,
                paperPlaneIcon = paperPlaneIcon,
                lightningIcon = lightningIcon,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SettingsSectionHeader(
    title: String,
    isDarkMode: Boolean,
    primaryColor: Color
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = primaryColor,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
    )
}

@Composable
fun LanguageOptionRow(
    languageOption: AppLanguage,
    selected: Boolean,
    onSelect: () -> Unit,
    textPrimaryColor: Color,
    textSecondaryColor: Color,
    primaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = languageOption.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = textPrimaryColor,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = if (languageOption == AppLanguage.ENGLISH) "United States" else "ایران",
                style = MaterialTheme.typography.bodySmall,
                color = textSecondaryColor
            )
        }
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = primaryColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun FloatingGlassmorphicNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    locales: Locales,
    isDarkMode: Boolean
) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    
    val barWidth = 330.dp
    val barHeight = 56.dp
    val lensHeight = 64.dp
    
    // Tab center positions in Dp & Px
    val homeCenterDp = 55.dp
    val localCenterDp = 165.dp
    val settingsCenterDp = 275.dp
    
    val homePx = with(density) { homeCenterDp.toPx() }
    val localPx = with(density) { localCenterDp.toPx() }
    val settingsPx = with(density) { settingsCenterDp.toPx() }
    
    val minPx = with(density) { 45.dp.toPx() }
    val maxPx = with(density) { 285.dp.toPx() }

    val targetPx = when (currentTab) {
        AppTab.HOME -> homePx
        AppTab.LOCAL -> localPx
        AppTab.SETTINGS -> settingsPx
    }
    
    val animatableCenterX = remember { Animatable(targetPx) }
    val animatableCenterY = remember { Animatable(0f) }
    
    var isDragging by remember { mutableStateOf(false) }
    var lastDragAmountX by remember { mutableStateOf(0f) }
    var lastDragAmountY by remember { mutableStateOf(0f) }
    
    // Dynamic inflation scale on drag/touch (grows larger & overflows bar boundary!)
    val inflationScale by animateFloatAsState(
        targetValue = if (isDragging) 1.28f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.48f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "inflationScale"
    )

    // Ripple effect on tap / snap
    var rippleTrigger by remember { mutableStateOf(0) }
    val rippleProgress by animateFloatAsState(
        targetValue = rippleTrigger.toFloat(),
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "ripple"
    )

    // Sync position when tab changes programmatically or by clicking
    LaunchedEffect(currentTab) {
        if (!isDragging) {
            rippleTrigger++
            animatableCenterX.animateTo(
                targetValue = targetPx,
                animationSpec = spring(
                    dampingRatio = 0.48f, // Bouncy iOS hyper-liquid spring
                    stiffness = Spring.StiffnessLow
                )
            )
            animatableCenterY.animateTo(0f, spring(0.5f, Spring.StiffnessLow))
        }
    }

    // Dynamic fluid liquid stretch & deformation
    val currentCenterX = animatableCenterX.value
    val currentCenterDp = with(density) { currentCenterX.toDp() }
    val currentCenterYDp = with(density) { animatableCenterY.value.toDp() }
    
    val distanceFromTargetPx = kotlin.math.abs(currentCenterX - targetPx)
    val dragVelocityFactor = if (isDragging) (kotlin.math.abs(lastDragAmountX) * 3.0f).coerceAtMost(45f) else 0f
    
    val stretchExtraDp = with(density) { 
        ((distanceFromTargetPx * 0.32f + dragVelocityFactor).coerceAtMost(60f)).toDp() 
    }
    val lensWidth = 96.dp + stretchExtraDp

    // Squash and stretch scale deformation physics
    val scaleX = if (isDragging) 1.10f + (lastDragAmountX.coerceIn(-12f, 12f) * 0.012f) else 1.0f
    val scaleY = if (isDragging) 0.88f + (lastDragAmountY.coerceIn(-12f, 12f) * 0.012f) else 1.0f

    // iOS 26 Specular Crystal Glass color accents (Pure Glass Frost, zero tint)
    val primaryGlow = Color(0xFFFFFFFF)
    val secondaryGlow = Color(0xAAFFFFFF)

    // Infinite ambient liquid shimmering pulse
    val infiniteTransition = rememberInfiniteTransition(label = "liquidShimmer")
    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPhase"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(lensHeight)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            // Spring bounce back on Y axis
                            coroutineScope.launch {
                                animatableCenterY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = 0.42f,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                            // Magnetic suction snap to nearest tab on X axis
                            val closestTab = when {
                                animatableCenterX.value < (homePx + localPx) / 2f -> AppTab.HOME
                                animatableCenterX.value > (localPx + settingsPx) / 2f -> AppTab.SETTINGS
                                else -> AppTab.LOCAL
                            }
                            val endTargetPx = when (closestTab) {
                                AppTab.HOME -> homePx
                                AppTab.LOCAL -> localPx
                                AppTab.SETTINGS -> settingsPx
                            }
                            coroutineScope.launch {
                                rippleTrigger++
                                animatableCenterX.animateTo(
                                    targetValue = endTargetPx,
                                    animationSpec = spring(
                                        dampingRatio = 0.45f, // Elastic suction snap
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }
                            onTabSelected(closestTab)
                        },
                        onDragCancel = {
                            isDragging = false
                            coroutineScope.launch {
                                animatableCenterY.animateTo(0f, spring(0.50f, Spring.StiffnessLow))
                                animatableCenterX.animateTo(targetPx, spring(0.50f, Spring.StiffnessLow))
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            lastDragAmountX = dragAmount.x
                            lastDragAmountY = dragAmount.y
                            val nextX = (animatableCenterX.value + dragAmount.x).coerceIn(minPx, maxPx)
                            val maxY = with(density) { 16.dp.toPx() } // ~2-3mm vertical motion allowance
                            val nextY = (animatableCenterY.value + dragAmount.y).coerceIn(-maxY, maxY)
                            coroutineScope.launch {
                                animatableCenterX.snapTo(nextX)
                                animatableCenterY.snapTo(nextY)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // 1. Base Futuristic Liquid Glass Capsule (iOS Specular Frosted Hull - Ultra Transparency 3% Opacity)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .align(Alignment.Center)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (isDarkMode) listOf(
                                Color(0x0EFFFFFF), // ~3-5% Ultra-Transparent Liquid Glass
                                Color(0x05FFFFFF),
                                Color(0x0A000000)
                            ) else listOf(
                                Color(0x22FFFFFF), // ~5-8% Ultra-Transparent Light Glass
                                Color(0x0CFFFFFF),
                                Color(0x18FFFFFF)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(28.dp), borderAlpha = 0.55f)
            )

            // 2. Interactive iOS Liquid Glass Lens (3D Inflating Glass Droplet with Chromatic Dispersion & Micro-Y displacement)
            Box(
                modifier = Modifier
                    .width(lensWidth)
                    .height(lensHeight)
                    .align(Alignment.CenterStart)
                    .offset(
                        x = currentCenterDp - (lensWidth / 2),
                        y = currentCenterYDp
                    )
                    .graphicsLayer {
                        this.scaleX = scaleX * inflationScale
                        this.scaleY = scaleY * inflationScale
                    }
                    .background(
                        brush = if (isDarkMode) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x12FFFFFF), // ~7% Ultra-Clear Glass Lens
                                    Color(0x05FFFFFF),
                                    Color(0x02000000)
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x1CFFFFFF), // ~11% Ultra-Clear Light Glass Lens
                                    Color(0x08FFFFFF),
                                    Color(0x04FFFFFF)
                                )
                            )
                        },
                        shape = RoundedCornerShape(32.dp)
                    )
                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(32.dp), borderAlpha = 0.40f)
                    .drawBehind {
                        val w = size.width
                        val h = size.height

                        // 1. Soft depth drop shadow behind inflated glass droplet when overflowing
                        if (isDragging) {
                            drawRoundRect(
                                color = Color.Black.copy(alpha = 0.35f),
                                topLeft = Offset(0f, 8.dp.toPx()),
                                size = Size(w, h),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(32.dp.toPx(), 32.dp.toPx())
                            )
                        }

                        // 2. Dark Internal Refraction Ring (Total Internal Reflection Rim - Pure WebGL Refraction)
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = if (isDarkMode) 0.55f else 0.35f)
                                ),
                                center = Offset(w / 2f, h / 2f),
                                radius = w * 0.55f
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(32.dp.toPx(), 32.dp.toPx())
                        )

                        // 3. Subtle Ultra-Clear Glass Rim Highlights
                        drawRoundRect(
                            color = Color.White.copy(alpha = if (isDragging) 0.20f else 0.10f),
                            topLeft = Offset(-1.2.dp.toPx(), -1.0.dp.toPx()),
                            size = Size(w, h),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.0.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(32.dp.toPx(), 32.dp.toPx())
                        )

                        // 4. Subtle Clear Liquid Ripple on Snap
                        val rFrac = rippleProgress - rippleProgress.toInt()
                        if (rFrac in 0.01f..0.99f) {
                            drawCircle(
                                color = Color.White.copy(alpha = (1f - rFrac) * 0.15f),
                                radius = (w * 0.9f) * rFrac,
                                center = Offset(w / 2f, h / 2f)
                            )
                        }
                    }
                    .clip(RoundedCornerShape(32.dp))
            )

            // 3. Proximity-aware Tab Items Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(lensHeight),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                val homeProximity = (1f - (kotlin.math.abs(currentCenterX - homePx) / (with(density) { 60.dp.toPx() }))).coerceIn(0f, 1f)
                NavBarItem(
                    selected = currentTab == AppTab.HOME,
                    proximity = homeProximity,
                    onClick = { onTabSelected(AppTab.HOME) },
                    icon = Icons.Default.Home,
                    label = locales.homeTab,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )

                // Local
                val localProximity = (1f - (kotlin.math.abs(currentCenterX - localPx) / (with(density) { 60.dp.toPx() }))).coerceIn(0f, 1f)
                NavBarItem(
                    selected = currentTab == AppTab.LOCAL,
                    proximity = localProximity,
                    onClick = { onTabSelected(AppTab.LOCAL) },
                    icon = Icons.Default.List,
                    label = locales.localTab,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )

                // Settings
                val settingsProximity = (1f - (kotlin.math.abs(currentCenterX - settingsPx) / (with(density) { 60.dp.toPx() }))).coerceIn(0f, 1f)
                NavBarItem(
                    selected = currentTab == AppTab.SETTINGS,
                    proximity = settingsProximity,
                    onClick = { onTabSelected(AppTab.SETTINGS) },
                    icon = Icons.Default.Settings,
                    label = locales.settingsTab,
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun NavBarItem(
    selected: Boolean,
    proximity: Float = 0f,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    isDarkMode: Boolean,
    modifier: Modifier = Modifier
) {
    // Dynamic lighting based on liquid lens proximity!
    val activeColor = if (isDarkMode) Color.White else Color(0xFF111827)
    val inactiveColor = if (isDarkMode) Color.White.copy(alpha = 0.45f) else Color.Black.copy(alpha = 0.45f)
    
    // Smoothly blend color based on proximity (0f = far, 1f = directly under liquid glass)
    val currentColor = lerp(inactiveColor, activeColor, proximity)
    
    // Scale up magnetically as liquid lens hovers over icon (convex optical magnification)
    val targetScale = 1.0f + (proximity * 0.28f)
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    val iconOffsetY = (-5 * proximity).dp

    Column(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = currentColor,
            modifier = Modifier
                .size(24.dp)
                .offset(y = iconOffsetY)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (proximity > 0.4f) FontWeight.Black else FontWeight.Bold,
            color = currentColor
        )
    }
}
