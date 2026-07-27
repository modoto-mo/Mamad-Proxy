package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
private class QrCodeAnalyzer(
    private val onQrScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private var isScanning = true

    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            try {
                val buffer = mediaImage.planes[0].buffer
                val data = ByteArray(buffer.remaining())
                buffer.get(data)
                val width = imageProxy.width
                val height = imageProxy.height
                
                val source = PlanarYUVLuminanceSource(
                    data, width, height, 0, 0, width, height, false
                )
                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                val result = MultiFormatReader().decode(binaryBitmap)
                if (result != null && result.text.isNotBlank()) {
                    isScanning = false
                    onQrScanned(result.text)
                }
            } catch (e: Exception) {
                // frame did not contain QR code
            }
        }
        imageProxy.close()
    }
}

@Composable
fun CameraQrScannerDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var cameraProviderRef by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(
                context,
                if (language == AppLanguage.ENGLISH) "Camera permission required to scan QR code" else "برای اسکن QR کد دسترسی دوربین لازم است",
                Toast.LENGTH_LONG
            ).show()
            onDismiss()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                cameraProviderRef?.unbindAll()
                cameraExecutor.shutdown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (!hasCameraPermission) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        cameraProviderRef = cameraProvider
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrText ->
                            executor.execute {
                                onQrScanned(qrText)
                                onDismiss()
                            }
                        })

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, executor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Scanning Viewfinder Overlay
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.Center)
                    .border(2.dp, Color(0xFF00E5FF), RoundedCornerShape(16.dp))
            )

            // Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Camera QR Scanner" else "اسکن دوربین QR کد پروکسی",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            // Bottom Instruction
            Text(
                text = if (language == AppLanguage.ENGLISH) "Position QR code inside frame" else "QR کد پروکسی را درون کادر قرار دهید",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
