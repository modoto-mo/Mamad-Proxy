package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun generateQrCodeBitmap(content: String, sizePx: Int = 600): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}

fun decodeQrCodeFromBitmap(bitmap: Bitmap): String? {
    return try {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val source = RGBLuminanceSource(width, height, pixels)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader()
        val result = reader.decode(binaryBitmap)
        result.text
    } catch (e: Exception) {
        null
    }
}

fun decodeQrCodeFromUri(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        if (bitmap != null) {
            decodeQrCodeFromBitmap(bitmap)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun QrCodeModalDialog(
    title: String,
    subtitle: String,
    qrContent: String,
    isDarkMode: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val copyIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                moveTo(16f, 1f); lineTo(4f, 1f); quadTo(3f, 1f, 3f, 2f); lineTo(3f, 14f)
                horizontalLineTo(5f); lineTo(5f, 3f); horizontalLineTo(16f); close()
                moveTo(19f, 5f); lineTo(8f, 5f); quadTo(7f, 5f, 7f, 6f); lineTo(7f, 20f)
                quadTo(7f, 21f, 8f, 21f); lineTo(19f, 21f); quadTo(20f, 21f, 20f, 20f)
                lineTo(20f, 6f); quadTo(20f, 5f, 19f, 5f); close()
                moveTo(18f, 19f); horizontalLineTo(9f); lineTo(9f, 7f); horizontalLineTo(18f); close()
            }
        }.build()
    }

    LaunchedEffect(qrContent) {
        withContext(Dispatchers.Default) {
            val bmp = generateQrCodeBitmap(qrContent)
            withContext(Dispatchers.Main) {
                qrBitmap = bmp
                isLoading = false
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF1E293B) else androidx.compose.ui.graphics.Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "QR Code Share" else "کد QR پروکسی",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color(0xFF0F172A)
                        )
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF94A3B8) else androidx.compose.ui.graphics.Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // High-contrast container for QR Code
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = androidx.compose.ui.graphics.Color(0xFF007AFF)
                        )
                    } else if (qrBitmap != null) {
                        Image(
                            bitmap = qrBitmap!!.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Failed to generate QR Code" else "خطا در ساخت QR کد",
                            color = androidx.compose.ui.graphics.Color.Red,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF94A3B8) else androidx.compose.ui.graphics.Color(0xFF64748B),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Proxy URL", qrContent)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.ENGLISH) "Copied to clipboard!" else "لینک کپی شد!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = copyIcon,
                            contentDescription = "Copy",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Copy" else "کپی",
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, qrContent)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(
                                sendIntent,
                                if (language == AppLanguage.ENGLISH) "Share Proxy" else "اشتراک گذاری پروکسی"
                            )
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Share" else "اشتراک",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

fun shareSubAsMdprxyFile(context: Context, subName: String, exportText: String, language: AppLanguage) {
    try {
        val safeFileName = subName.replace(Regex("[^a-zA-Z0-9_\\-\\u0600-\\u06FF]"), "_").ifBlank { "subscription" }
        val fileName = "${safeFileName}.mdprxy"
        val cacheFile = File(context.cacheDir, fileName)
        cacheFile.writeText(exportText)

        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cacheFile
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_SUBJECT, subName)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(
            shareIntent,
            if (language == AppLanguage.ENGLISH) "Share $fileName" else "اشتراک‌گذاری فایل $fileName"
        )
        context.startActivity(chooser)
    } catch (e: Exception) {
        Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ExportSubModalDialog(
    subName: String,
    exportText: String,
    isDarkMode: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var showQrForSub by remember { mutableStateOf(false) }
    val lineCount = remember(exportText) { if (exportText.isBlank()) 0 else exportText.lines().count { it.isNotBlank() } }

    val copyIcon = remember(isDarkMode) {
        ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black)) {
                moveTo(16f, 1f); lineTo(4f, 1f); quadTo(3f, 1f, 3f, 2f); lineTo(3f, 14f)
                horizontalLineTo(5f); lineTo(5f, 3f); horizontalLineTo(16f); close()
                moveTo(19f, 5f); lineTo(8f, 5f); quadTo(7f, 5f, 7f, 6f); lineTo(7f, 20f)
                quadTo(7f, 21f, 8f, 21f); lineTo(19f, 21f); quadTo(20f, 21f, 20f, 20f)
                lineTo(20f, 6f); quadTo(20f, 5f, 19f, 5f); close()
                moveTo(18f, 19f); horizontalLineTo(9f); lineTo(9f, 7f); horizontalLineTo(18f); close()
            }
        }.build()
    }

    if (showQrForSub) {
        QrCodeModalDialog(
            title = subName,
            subtitle = "$lineCount ${if (language == AppLanguage.ENGLISH) "Proxies in Sub" else "پروکسی در ساب"}",
            qrContent = exportText,
            isDarkMode = isDarkMode,
            language = language,
            onDismiss = { showQrForSub = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF1E293B) else androidx.compose.ui.graphics.Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Share" else "اشتراک‌گذاری",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color(0xFF0F172A)
                        )
                        Text(
                            text = "$subName ($lineCount ${if (language == AppLanguage.ENGLISH) "proxies" else "پروکسی"})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF94A3B8) else androidx.compose.ui.graphics.Color(0xFF64748B)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable preview box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDarkMode) androidx.compose.ui.graphics.Color(0x1AFFFFFF) else androidx.compose.ui.graphics.Color(0x0F000000),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 200.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = exportText.ifBlank { if (language == AppLanguage.ENGLISH) "No proxies in this sub" else "پروکسی در این ساب وجود ندارد" },
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFFE2E8F0) else androidx.compose.ui.graphics.Color(0xFF334155)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Copy All
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Exported Sub Proxies", exportText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.ENGLISH) "Copied $lineCount proxies to clipboard!" else "$lineCount پروکسی در حافظه کپی شد!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = copyIcon,
                            contentDescription = "Copy",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Copy All" else "کپی همه",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Share Text
                    OutlinedButton(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, exportText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(
                                sendIntent,
                                if (language == AppLanguage.ENGLISH) "Share - $subName" else "اشتراک‌گذاری - $subName"
                            )
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Text",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Share Text" else "اشتراک متن",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Share as MDPRXY File Button
                Button(
                    onClick = {
                        shareSubAsMdprxyFile(context, subName, exportText, language)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) androidx.compose.ui.graphics.Color(0xFF007AFF) else androidx.compose.ui.graphics.Color(0xFF0284C7)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share MDPRXY",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Share as .mdprxy File" else "اشتراک‌گذاری به صورت فایل .mdprxy",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // QR Code Button for Sub
                OutlinedButton(
                    onClick = { showQrForSub = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Generate Sub QR Code" else "ساخت QR کد ساب",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
