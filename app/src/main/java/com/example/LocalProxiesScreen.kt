package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LocalProxiesScreen(
    viewModel: ProxyViewModel,
    isDarkMode: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locales = remember(language) { Locales(language) }

    val subscriptions by viewModel.subscriptions.collectAsState()
    val selectedSubId by viewModel.selectedSubscriptionId.collectAsState()
    val localProxies by viewModel.localProxies.collectAsState()
    val savedProxies by viewModel.savedProxies.collectAsState()
    val isTestingAllLocal by viewModel.isTestingAllLocal.collectAsState()
    val isLocalAutoSortEnabled by viewModel.isLocalAutoSortEnabled.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var showCameraScanner by remember { mutableStateOf(false) }
    var subToDelete by remember { mutableStateOf<LocalSubscriptionEntity?>(null) }
    var exportedSubData by remember { mutableStateOf<Pair<String, String>?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val galleryQrPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val decoded = decodeQrCodeFromUri(context, it)
            if (!decoded.isNullOrBlank()) {
                val targetSub = selectedSubId ?: DefaultLocalProxies.DEFAULT_SUB_ID
                viewModel.importProxiesToSub(targetSub, decoded) { count ->
                    if (count > 0) {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ENGLISH) "$count proxies added to sub!" else "$count پروکسی به ساب افزوده شد!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ENGLISH) "No valid proxies found in QR code" else "پروکسی معتبری در تصویر QR کد یافت نشد",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    if (language == AppLanguage.ENGLISH) "No valid QR code found in selected image" else "QR کد معتبری در تصویر انتخابی یافت نشد",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val primaryColor = if (isDarkMode) Color(0xFF00E5FF) else Color(0xFF007AFF)
    val textPrimaryColor = if (isDarkMode) Color.White else Color(0xFF0F172A)
    val textSecondaryColor = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    val copyIcon = remember {
        ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) Color.White else Color.Black)) {
                moveTo(16f, 1f); lineTo(4f, 1f); quadTo(3f, 1f, 3f, 2f); lineTo(3f, 14f)
                horizontalLineTo(5f); lineTo(5f, 3f); horizontalLineTo(16f); close()
                moveTo(19f, 5f); lineTo(8f, 5f); quadTo(7f, 5f, 7f, 6f); lineTo(7f, 20f)
                quadTo(7f, 21f, 8f, 21f); lineTo(19f, 21f); quadTo(20f, 21f, 20f, 20f)
                lineTo(20f, 6f); quadTo(20f, 5f, 19f, 5f); close()
                moveTo(18f, 19f); horizontalLineTo(9f); lineTo(9f, 7f); horizontalLineTo(18f); close()
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
                moveTo(12f, 2f); arcTo(10f, 10f, 0f, false, false, 2f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 22f); arcTo(10f, 10f, 0f, false, false, 22f, 12f)
                arcTo(10f, 10f, 0f, false, false, 12f, 2f); close()
                moveTo(12f, 4f); arcTo(8f, 8f, 0f, false, true, 18.5f, 7.5f)
                lineTo(16.5f, 9.5f); arcTo(5f, 5f, 0f, false, false, 12f, 7f)
                arcTo(5f, 5f, 0f, false, false, 7f, 12f); arcTo(5f, 5f, 0f, false, false, 12f, 17f)
                arcTo(5f, 5f, 0f, false, false, 15.5f, 15.5f); lineTo(17.5f, 17.5f)
                arcTo(8f, 8f, 0f, false, true, 12f, 20f); arcTo(8f, 8f, 0f, false, true, 4f, 12f)
                arcTo(8f, 8f, 0f, false, true, 12f, 4f); close()
                moveTo(12f, 9f); arcTo(3f, 3f, 0f, false, false, 9f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 15f); arcTo(3f, 3f, 0f, false, false, 15f, 12f)
                arcTo(3f, 3f, 0f, false, false, 12f, 9f); close()
                moveTo(13f, 11f); lineTo(16f, 8f); lineTo(17f, 9f); lineTo(14f, 12f); close()
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
                moveTo(2f, 21f); lineTo(23f, 12f); lineTo(2f, 3f)
                lineTo(2f, 10f); lineTo(17f, 12f); lineTo(2f, 14f); close()
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
                moveTo(7f, 2f); lineTo(17f, 2f); lineTo(12f, 11f)
                lineTo(19f, 11f); lineTo(9f, 22f); lineTo(12f, 13f); close()
            }
        }.build()
    }

    val filteredProxies = remember(localProxies, searchQuery) {
        if (searchQuery.isBlank()) {
            localProxies
        } else {
            localProxies.filter {
                it.server.contains(searchQuery, ignoreCase = true) ||
                        it.port.toString().contains(searchQuery)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Header Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Local Proxies" else "پروکسی‌های لوکال",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimaryColor
                    )
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Manage & import custom subscription files" else "مدیریت و وارد کردن ساب‌ و فایل‌های پروکسی",
                        fontSize = 12.sp,
                        color = textSecondaryColor
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Sub",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Add Sub" else "افزودن ساب",
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Subscriptions Row (ساب‌ بندی)
        item {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Subscriptions" else "لیست ساب‌ها",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textSecondaryColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (subscriptions.isEmpty()) {
                GlassMorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    isDarkMode = isDarkMode
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "No subscriptions found. Click 'Add Sub' to import proxies." else "هیچ سابی یافت نشد. روی 'افزودن ساب' کلیک کنید.",
                        color = textSecondaryColor,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )
                }
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    subscriptions.forEach { sub ->
                        val isSelected = sub.id == selectedSubId
                        val isDefaultSub = (sub.id == DefaultLocalProxies.DEFAULT_SUB_ID)
                        val chipBg = if (isSelected) primaryColor.copy(alpha = 0.25f) else (if (isDarkMode) Color(0x1AFFFFFF) else Color(0x33FFFFFF))
                        val borderAlpha = if (isSelected) 0.8f else 0.2f

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(chipBg)
                                .liquidGlassBorder(isDarkMode, RoundedCornerShape(16.dp), borderAlpha = borderAlpha)
                                .clickable { viewModel.selectSubscription(sub.id) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null,
                                    tint = if (isSelected) primaryColor else textSecondaryColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = sub.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) primaryColor else textPrimaryColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = {
                                        viewModel.exportSubscription(sub.id) { name, text ->
                                            exportedSubData = Pair(name, text)
                                        }
                                    },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Export sub",
                                        tint = primaryColor,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                if (!isDefaultSub) {
                                    Spacer(modifier = Modifier.width(2.dp))
                                    IconButton(
                                        onClick = { subToDelete = sub },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete sub",
                                            tint = textSecondaryColor.copy(alpha = 0.7f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Camera QR scanner action row above proxies list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showCameraScanner = true },
                    modifier = Modifier.weight(1.2f),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Camera QR",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Add via Camera QR" else "افزودن با دوربین QR",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { galleryQrPickerLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor)
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "QR Image" else "انتخاب عکس QR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Active Subscription Controls
        item {
            if (selectedSubId != null) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${filteredProxies.size} ${if (language == AppLanguage.ENGLISH) "Proxies" else "پروکسی"}",
                                fontSize = 13.sp,
                                color = textSecondaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Auto-Sort Toggle Button
                            FilterChip(
                                selected = isLocalAutoSortEnabled,
                                onClick = { viewModel.toggleLocalAutoSort() },
                                label = {
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Auto-Sort" else "سورت خودکار",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ElegantGreen.copy(alpha = 0.2f),
                                    selectedLabelColor = ElegantGreen,
                                    selectedLeadingIconColor = ElegantGreen,
                                    containerColor = if (isDarkMode) Color(0x1AFFFFFF) else Color(0x33FFFFFF),
                                    labelColor = textSecondaryColor
                                )
                            )

                            // Manual Test All Button
                            Button(
                                onClick = { viewModel.testAllLocalProxies() },
                                enabled = !isTestingAllLocal && localProxies.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = primaryColor
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(10.dp), borderAlpha = 0.2f)
                                    .background(if (isDarkMode) Color(0x0CFFFFFF) else Color(0x1F000000), RoundedCornerShape(10.dp))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Test Speeds",
                                        modifier = Modifier.size(14.dp),
                                        tint = primaryColor
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isTestingAllLocal) {
                                            if (language == AppLanguage.ENGLISH) "Testing..." else "در حال تست..."
                                        } else {
                                            locales.testAll
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Export Sub Button
                            Button(
                                onClick = {
                                    val selId = selectedSubId
                                    if (selId != null) {
                                        viewModel.exportSubscription(selId) { name, text ->
                                            exportedSubData = Pair(name, text)
                                        }
                                    }
                                },
                                enabled = selectedSubId != null && localProxies.isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = primaryColor
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .liquidGlassBorder(isDarkMode, RoundedCornerShape(10.dp), borderAlpha = 0.2f)
                                    .background(if (isDarkMode) Color(0x0CFFFFFF) else Color(0x1F000000), RoundedCornerShape(10.dp))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Export Sub",
                                        modifier = Modifier.size(14.dp),
                                        tint = primaryColor
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (language == AppLanguage.ENGLISH) "Share" else "اشتراک‌گذاری",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(locales.searchPlaceholder, fontSize = 12.sp, color = textSecondaryColor)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = textSecondaryColor, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = if (searchQuery.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = textSecondaryColor, modifier = Modifier.size(16.dp))
                            }
                        }
                    } else null,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0x1AFFFFFF) else Color(0x33FFFFFF),
                        unfocusedContainerColor = if (isDarkMode) Color(0x0CFFFFFF) else Color(0x1AFFFFFF),
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = textPrimaryColor,
                        unfocusedTextColor = textPrimaryColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Local Proxies List
        if (filteredProxies.isEmpty() && selectedSubId != null) {
            item {
                GlassMorphicCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    isDarkMode = isDarkMode
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "No proxies found in this sub." else "هیچ پروکسی در این ساب یافت نشد.",
                        color = textSecondaryColor,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                }
            }
        } else {
            items(filteredProxies, key = { it.id }) { proxy ->
                ProxyItemCard(
                    proxy = proxy,
                    locales = locales,
                    isDarkMode = isDarkMode,
                    isSaved = savedProxies.any { it.rawUrl == proxy.rawUrl },
                    onToggleSave = { viewModel.toggleSaveProxy(proxy) },
                    onTestPing = { viewModel.testLocalProxyPing(proxy.id) },
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
                    lightningIcon = lightningIcon,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Add Subscription Dialog
    if (showAddDialog) {
        AddSubscriptionDialog(
            isDarkMode = isDarkMode,
            language = language,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, content ->
                viewModel.createSubscription(name, content)
                showAddDialog = false
            }
        )
    }

    // Camera QR Scanner Dialog
    if (showCameraScanner) {
        CameraQrScannerDialog(
            language = language,
            onDismiss = { showCameraScanner = false },
            onQrScanned = { qrText ->
                val targetSub = selectedSubId ?: DefaultLocalProxies.DEFAULT_SUB_ID
                viewModel.importProxiesToSub(targetSub, qrText) { count ->
                    if (count > 0) {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ENGLISH) "$count proxies added to sub!" else "$count پروکسی به ساب افزوده شد!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ENGLISH) "No valid proxies found in QR code" else "پروکسی معتبری در QR کد اسکن شده یافت نشد",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }

    // Delete Subscription Confirmation Dialog
    subToDelete?.let { sub ->
        AlertDialog(
            onDismissRequest = { subToDelete = null },
            title = {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Delete Subscription" else "حذف ساب",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "Are you sure you want to delete '${sub.name}' and all its proxies?"
                    else
                        "آیا از حذف ساب «${sub.name}» و تمام پروکسی‌های آن اطمینان دارید؟"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSubscription(sub.id)
                        subToDelete = null
                    }
                ) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Delete" else "حذف",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { subToDelete = null }) {
                    Text(if (language == AppLanguage.ENGLISH) "Cancel" else "انصراف")
                }
            }
        )
    }

    if (exportedSubData != null) {
        ExportSubModalDialog(
            subName = exportedSubData!!.first,
            exportText = exportedSubData!!.second,
            isDarkMode = isDarkMode,
            language = language,
            onDismiss = { exportedSubData = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionDialog(
    isDarkMode: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (name: String, content: String) -> Unit
) {
    val context = LocalContext.current
    var subName by remember { mutableStateOf("") }
    var rawText by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val extractedName = extractFileNameFromUri(context, it)
                if (extractedName.isNotBlank()) {
                    subName = extractedName
                }
                val inputStream = context.contentResolver.openInputStream(it)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                rawText = stringBuilder.toString()
                Toast.makeText(
                    context,
                    if (language == AppLanguage.ENGLISH) "File '$extractedName' loaded successfully!" else "فایل «$extractedName» با موفقیت خوانده شد!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    if (language == AppLanguage.ENGLISH) "Error reading file" else "خطا در خواندن فایل",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val qrPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val decoded = decodeQrCodeFromUri(context, it)
            if (!decoded.isNullOrBlank()) {
                rawText = if (rawText.isBlank()) decoded else "$rawText\n$decoded"
                if (subName.isBlank()) {
                    subName = if (language == AppLanguage.ENGLISH) "QR Sub" else "ساب QR"
                }
                Toast.makeText(
                    context,
                    if (language == AppLanguage.ENGLISH) "QR Code scanned! Proxy imported." else "QR کد اسکن شد! پروکسی وارد گردید.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    if (language == AppLanguage.ENGLISH) "No valid QR code found in selected image" else "QR کد معتبری در تصویر انتخابی یافت نشد",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Import / Create Subscription" else "افزودن ساب / پروکسی‌ها",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = subName,
                    onValueChange = { subName = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Subscription Name" else "نام ساب") },
                    placeholder = { Text(if (language == AppLanguage.ENGLISH) "e.g. My Proxies" else "مثلاً: پروکسی‌های شخصی") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { filePickerLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Pick .mdprxy / .txt File" else "انتخاب فایل mdprxy یا txt",
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = { qrPickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Scan QR Image" else "انتخاب عکس QR",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "Or Paste Proxy Links:" else "یا لینک‌های پروکسی را اینجا پیست کنید:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = rawText,
                    onValueChange = { rawText = it },
                    placeholder = { Text("https://t.me/proxy?server=...\ntg://proxy?server=...") },
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (rawText.isBlank()) {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ENGLISH) "Please import a file or paste proxy links!" else "لطفاً فایل وارد کنید یا لینک پروکسی‌ها را پیست کنید!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onConfirm(subName, rawText)
                    }
                }
            ) {
                Text(if (language == AppLanguage.ENGLISH) "Save Sub" else "ذخیره ساب")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.ENGLISH) "Cancel" else "انصراف")
            }
        }
    )
}

fun extractFileNameFromUri(context: Context, uri: Uri): String {
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
    return cleanName
}
