[🇮🇷 مطالعه به زبان فارسی](README_FA.md)

<br/>

<div align="center">

  <img src="app/src/main/res/drawable/ic_launcher_foreground_asset_1783275886479.jpg" width="170" height="170" style="border-radius: 50%; box-shadow: 0 10px 30px rgba(0,0,0,0.3);" alt="Mamad Proxy Logo" />

  # ⚡ MAMAD PROXY ⚡
  ### *The Ultimate MTProto Proxy Manager & Latency Benchmarking Engine for Telegram*

  <p align="center">
    <a href="https://github.com/modoto-mo/Mamad-Proxy/releases/latest">
      <img src="https://img.shields.io/github/v/release/modoto-mo/Mamad-Proxy?style=for-the-badge&logo=github&color=FF6F00" alt="Latest Release" />
    </a>
    <a href="https://t.me/mamad1vpn">
      <img src="https://img.shields.io/badge/Telegram-@mamad1vpn-26A5E4?style=for-the-badge&logo=telegram" alt="Telegram Channel" />
    </a>
    <a href="https://kotlinlang.org">
      <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin" alt="Kotlin" />
    </a>
    <a href="https://developer.android.com/jetpack/compose">
      <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=for-the-badge&logo=jetpackcompose" alt="Jetpack Compose" />
    </a>
    <a href="https://developer.android.com/about/versions/nougat">
      <img src="https://img.shields.io/badge/Android-7.0%2B-3DDC84?style=for-the-badge&logo=android" alt="Android Version" />
    </a>
    <a href="LICENSE">
      <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="MIT License" />
    </a>
  </p>

  <br/>

  <table>
    <tr>
      <td align="center">
        <a href="https://github.com/modoto-mo/Mamad-Proxy/releases/latest">
          <b>🚀 DOWNLOAD APK (RELEASES)</b>
        </a>
      </td>
      <td align="center">
        <a href="https://t.me/mamad1vpn">
          <b>📢 TELEGRAM CHANNEL (@mamad1vpn)</b>
        </a>
      </td>
      <td align="center">
        <a href="https://t.me/frzmmd">
          <b>💬 DEVELOPER SUPPORT (@frzmmd)</b>
        </a>
      </td>
    </tr>
  </table>

</div>

---

<br/>

## 🎯 Overview

<table>
  <tr>
    <td>
      <h3>⚡ High-Speed Connection & Benchmarking</h3>
      <b>Mamad Proxy</b> is a next-generation native Android application crafted to eliminate the hassle of dead links, slow speeds, and cluttered proxy lists in Telegram. Equipped with a real-time <b>TCP/Socket Ping Engine</b>, Mamad Proxy benchmarks MTProto proxies instantly and sorts them from fastest to slowest.
      <br/><br/>
      Built using <b>Modern Android Development (MAD)</b> standards, <b>Jetpack Compose</b>, <b>Material Design 3</b>, and <b>Room Database</b> for zero-lag, offline-first performance.
    </td>
  </tr>
</table>

<br/>

---

## 🔥 Key Highlights

<div align="center">

| Feature | Description | Tech Highlight |
| :--- | :--- | :--- |
| ⚡ **Real-Time Speed Benchmark** | Measures exact TCP socket response latency (ms) with live speed indicators. | Coroutines + Socket Ping |
| 🔀 **Smart Auto-Sorting** | Sorts proxies instantly by lowest ping and filters out inactive links. | Flow + StateFlow |
| 🚀 **One-Tap Telegram Connect** | Connects seamlessly to Telegram using native `tg://proxy` protocol handlers. | Android Intents |
| 📁 **Subscription Hub** | Manage multiple subscriptions, auto-update links, and auto-parse `.mdprxy` files. | Room DB + Auto-Naming |
| 📷 **Camera & QR Scanner** | Instant scanning of Proxy QR codes and generating QR codes for easy sharing. | CameraX + ZXing |
| 📤 **One-Click Share** | Export individual proxies or entire subscription links effortlessly. | Native Share Sheet |
| 🎨 **Material 3 & Dark Theme** | Glassmorphism card aesthetics with full Light and Dark theme switching. | Dynamic Color / M3 |
| 🌐 **Bilingual (EN / FA)** | Native support for English and Persian (فارسی) with instant runtime toggle. | Localization Engine |

</div>

<br/>

---

## 📸 App Showcase

<div align="center">

<table>
  <tr>
    <td align="center" width="25%">
      <b>📱 Home & Proxy List</b><br/><br/>
      <img src="docs/screenshots/home.png" width="210" alt="Home Screen" />
    </td>
    <td align="center" width="25%">
      <b>⚡ Ping Benchmark</b><br/><br/>
      <img src="docs/screenshots/ping.png" width="210" alt="Ping Benchmark" />
    </td>
    <td align="center" width="25%">
      <b>📷 QR Scanner</b><br/><br/>
      <img src="docs/screenshots/qr.png" width="210" alt="QR Scanner" />
    </td>
    <td align="center" width="25%">
      <b>⚙️ Subscriptions</b><br/><br/>
      <img src="docs/screenshots/settings.png" width="210" alt="Settings Screen" />
    </td>
  </tr>
</table>

<sub>*(Place your app screenshots inside <code>docs/screenshots/</code> to render them here)*</sub>

</div>

<br/>

---

## 🛠️ Architecture & Tech Stack

<div align="center">

```
  ┌─────────────────────────────────────────────────────────┐
  │                 Jetpack Compose (UI)                    │
  ├─────────────────────────────────────────────────────────┤
  │            ViewModel + StateFlow + Coroutines           │
  ├─────────────────────────────────────────────────────────┤
  │         Room Database   │   Socket & Ping Engine        │
  └─────────────────────────────────────────────────────────┘
```

</div>

<details>
<summary><b>🔍 View Full Technical Stack</b></summary>

<br/>

- **Language:** 100% Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
- **Asynchronous Engine:** Kotlin Coroutines & StateFlow
- **Local Database:** Room Database with KSP (Kotlin Symbol Processing)
- **Scanning & Imaging:** CameraX + ZXing Code Engine
- **Network & Parsing:** OkHttp + Retrofit + Moshi
- **Testing:** Robolectric, Roborazzi Screenshot Testing, JUnit4
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`)

</details>

<br/>

---

## 🚀 How to Build & Install

<details>
<summary><b>🛠️ Step-by-Step Developer Setup</b></summary>

<br/>

### Prerequisites
- **Android Studio:** Ladybug / Koala or newer
- **JDK:** Java 17 / 21
- **Android SDK:** API Level 36 (Minimum SDK: 24 / Android 7.0)

### Quick Start Commands
```bash
# 1. Clone the repository
git clone https://github.com/modoto-mo/Mamad-Proxy.git
cd Mamad-Proxy

# 2. Build Debug APK
./gradlew assembleDebug

# 3. Run Unit Tests
./gradlew testDebugUnitTest
```

The compiled APK will be available at:  
`<project-root>/app/build/outputs/apk/debug/app-debug.apk`

</details>

<br/>

---

## 📢 Telegram & Developer Contact

<div align="center">

<table>
  <tr>
    <td align="center" width="50%">
      <h3>✈️ Official Telegram Channel</h3>
      Get high-speed MTProto proxies, VPN updates, and news.<br/><br/>
      <a href="https://t.me/mamad1vpn"><b>👉 Join @mamad1vpn</b></a>
    </td>
    <td align="center" width="50%">
      <h3>💬 Developer Contact</h3>
      Feedback, feature requests, or questions?<br/><br/>
      <a href="https://t.me/frzmmd"><b>👉 Message @frzmmd</b></a>
    </td>
  </tr>
</table>

</div>

<br/>

---

<div align="center">
  Made with ❤️ For The Brave People Of Iran.
</div>
