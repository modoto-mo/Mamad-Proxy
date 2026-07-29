[🇮🇷 مطالعه به زبان فارسی](README_FA.md)

<br/>

<div align="center">

<img src="app/src/main/res/drawable/ic_launcher_foreground_asset_1783275886479.jpg" width="170" height="170" style="border-radius: 40px;" alt="Mamad Proxy Logo" />

# 🚀 Mamad Proxy

### Next-Gen High-Performance MTProto Proxy Manager & Latency Tester for Telegram

[![Releases](https://img.shields.io/github/v/release/modoto-mo/Mamad-Proxy?style=for-the-badge&logo=github&color=FF6F00)](https://github.com/modoto-mo/Mamad-Proxy/releases/latest)
[![Telegram Channel](https://img.shields.io/badge/Telegram_Channel-@mamad1vpn-26A5E4.svg?style=for-the-badge&logo=telegram)](https://t.me/mamad1vpn)
[![Version](https://img.shields.io/badge/Version-1.0.4-blue.svg?style=for-the-badge&logo=android)](app/build.gradle.kts)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![MinSDK](https://img.shields.io/badge/Min_SDK-24_(Android_7.0)-3DDC84.svg?style=for-the-badge&logo=android)](https://developer.android.com/about/versions/nougat)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

<br/>

[![Download APK](https://img.shields.io/badge/📥_Download_Latest_APK-Releases-success?style=for-the-badge&logo=android&logoColor=white)](https://github.com/modoto-mo/Mamad-Proxy/releases/latest)

---

</div>

## 📦 Direct Download (Releases)

Want to install the app right away without building from source?  
👉 **[Download the Latest Release APK](https://github.com/modoto-mo/Mamad-Proxy/releases/latest)** directly from GitHub Releases.

---

## 📢 Community & Support

- ✈️ **Telegram Channel**: [@mamad1vpn](https://t.me/mamad1vpn) 
- 💬 **Developer Contact**: [@frzmmd](https://t.me/frzmmd)

---

## 🌟 About the Project

**Mamad Proxy** is a sleek, modern, and powerful native Android application designed to give Telegram users complete control over their connection speed, stability, and proxy management. 

Connecting to Telegram via MTProto proxies often involves dealing with dead links, slow response times, and cluttered proxy lists. **Mamad Proxy** solves this by providing **real-time TCP/socket ping benchmarking**, **automatic proxy sorting by latency**, **subscription management**, and **instant one-tap Telegram integration**.

Built from the ground up using **Modern Android Development (MAD)** principles, Jetpack Compose, Material Design 3, and Room DB, Mamad Proxy delivers a fluid, responsive, and visual experience.

---

## ✨ Key Features

- ⚡ **Real-Time Speed & Ping Testing**: Measure real-time socket latency (ms) for any MTProto proxy with color-coded speed indicators (Green for fast, Yellow for medium, Red for slow).
- 🔀 **Smart Sorting & Filtering**: Automatically sort proxies from fastest to slowest, or filter out inactive proxies with a single click.
- 🚀 **Direct One-Tap Connection**: Connect instantly to Telegram using native `tg://proxy` protocol handlers or external web launchers.
- 📁 **Advanced Subscription Management**: Organize proxies into subscription groups, update links dynamically, or import proxy lists directly from files (`.mdprxy`, raw text, or URLs).
- 📦 **Automated File Import**: Importing `.mdprxy` files automatically generates dedicated subscription lists named after the source file without tedious manual setups.
- 📷 **Integrated QR Code Scanner & Generator**: Scan proxy QR codes in milliseconds using CameraX + ZXing, or generate custom QR codes to share proxies and full subscriptions with friends.
- 📤 **Instant Sharing**: Share individual proxies or entire subscriptions easily with native Android share sheets.
- 🎨 **Material 3 & Dark Mode**: Beautiful, modern UI featuring smooth transitions, glassmorphic card aesthetics, and full Dark/Light theme switching.
- 🌐 **Bilingual Support**: Fully localized in both **English** and **Persian (فارسی)** with seamless runtime language switching.
- 🔐 **Privacy-First & Offline Storage**: All proxy lists and local subscriptions are stored securely on-device using SQLite and Room DB. No tracking or external data collection.

---

## 📸 App Screenshots

<div align="center">

| 📱 Home & Proxy List | ⚡ Ping Benchmark | 📷 QR Scanner & Generator | ⚙️ Subscriptions & Settings |
| :---: | :---: | :---: | :---: |
| <img src="docs/screenshots/home.png" width="220" alt="Home Screen" /> | <img src="docs/screenshots/ping.png" width="220" alt="Ping Benchmark" /> | <img src="docs/screenshots/qr.png" width="220" alt="QR Scanner" /> | <img src="docs/screenshots/settings.png" width="220" alt="Settings Screen" /> |

*(Place your app screenshots inside `docs/screenshots/` to display them here)*

</div>

---

## 🛠️ Tech Stack & Architecture

Mamad Proxy follows **Clean Architecture** and the **MVVM (Model-View-ViewModel)** pattern with Unidirectional Data Flow (UDF).

| Layer | Technology / Library |
| :--- | :--- |
| **Language** | 100% [Kotlin](https://kotlinlang.org/) |
| **UI Framework** | [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3 |
| **Architecture** | MVVM, Kotlin Coroutines, StateFlow, ViewModel |
| **Local Database** | [Room DB](https://developer.android.com/training/data-storage/room) with KSP (Kotlin Symbol Processing) |
| **Camera & QR** | [CameraX](https://developer.android.com/training/camerax) + [ZXing Core](https://github.com/zxing/zxing) |
| **Network & Parsing** | OkHttp, Retrofit, Moshi |
| **Testing** | Robolectric, Roborazzi (Visual Screenshot Testing), JUnit4 |
| **Build Tooling** | Gradle (Kotlin DSL `.gradle.kts`), Android Gradle Plugin 8.x |

---

## 🚀 How to Build & Run

### Prerequisites

- **Android Studio**: Ladybug / Koala or newer recommended
- **JDK**: Java 17 or Java 21
- **Android SDK**: API Level 36 (Minimum SDK: 24 / Android 7.0)

### Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/modoto-mo/Mamad-Proxy.git
   cd Mamad-Proxy
   ```

2. **Open in Android Studio:**
   - Open Android Studio and select **Open**.
   - Navigate to the `mamad-proxy` folder and click OK.
   - Wait for Gradle sync to complete.

3. **Build and Run via CLI (Optional):**
   ```bash
   # Build Debug APK
   ./gradlew assembleDebug

   # Run Unit Tests
   ./gradlew testDebugUnitTest
   ```

4. **Install on Device:**
   - Connect your Android device or start an emulator.
   - Click **Run** (`Shift + F10`) in Android Studio or install the APK located at:
     `app/build/outputs/apk/debug/app-debug.apk`

---

## 📂 Project Structure

```
mamad-proxy/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt        # Main Entry & Navigation
│   │   │   │   ├── ProxyViewModel.kt      # Core State Management & Ping Logic
│   │   │   │   ├── LocalProxiesScreen.kt  # Proxy List & Subscription UI
│   │   │   │   ├── QrCodeGenerator.kt     # QR Generation & Scanning
│   │   │   │   ├── LocalProxyDao.kt       # Room Database DAO
│   │   │   │   └── AppDatabase.kt         # Room Database Config
│   │   │   └── res/                      # Drawables, Strings, XMLs
│   │   └── test/                          # Unit & Roborazzi Screenshot Tests
│   └── build.gradle.kts                   # App Dependencies & Config
├── gradle/                                # Gradle Wrapper & Version Catalogs
├── README.md                              # English Documentation
└── README_FA.md                           # Persian Documentation
```

---

## 🤝 Contributing

Contributions are welcome! If you'd like to improve Mamad Proxy, feel free to open an issue or submit a Pull Request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AwesomeFeature`)
3. Commit your Changes (`git commit -m 'Add some AwesomeFeature'`)
4. Push to the Branch (`git push origin feature/AwesomeFeature`)
5. Open a Pull Request

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">
  Made with ❤️ For The Brave People Of Iran.
</div>
