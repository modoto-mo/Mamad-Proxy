<div align="center">

<!-- HERO LOGO & TITLE -->
<img src="https://raw.githubusercontent.com/modoto-mo/Mamad-Proxy/main/app/src/main/res/drawable/ic_launcher_foreground_asset_1783275886479.jpg" alt="Mamad Proxy Banner" width="100%" style="border-radius: 20px; margin-bottom: 20px;" />

# ⚡ MAMAD PROXY ⚡
### 💎 *Next-Gen Glassmorphic Telegram Proxy Manager & Speed Tester for Android* 💎

<p align="center">
  <a href="https://github.com/modoto-mo/Mamad-Proxy/stargazers"><img src="https://img.shields.io/github/stars/modoto-mo/Mamad-Proxy?style=for-the-badge&logo=github&color=FF4500" alt="Stars"></a>
  <a href="https://github.com/modoto-mo/Mamad-Proxy/network/members"><img src="https://img.shields.io/github/forks/modoto-mo/Mamad-Proxy?style=for-the-badge&logo=github&color=FF8C00" alt="Forks"></a>
  <a href="https://github.com/modoto-mo/Mamad-Proxy/releases"><img src="https://img.shields.io/github/v/release/modoto-mo/Mamad-Proxy?style=for-the-badge&logo=android&color=00B0FF" alt="Release"></a>
  <a href="https://t.me/mamad1vpn"><img src="https://img.shields.io/badge/Telegram-Mamad_Config-26A5E4?style=for-the-badge&logo=telegram" alt="Telegram Channel"></a>
  <a href="LICENSE"><img src="https://img.shields.io/github/license/modoto-mo/Mamad-Proxy?style=for-the-badge&color=00C853" alt="License"></a>
</p>

<p align="center">
  <b>[ 🇮🇷 فارسی ]</b> &nbsp;•&nbsp; <b>[ 🇬🇧 English ]</b> &nbsp;•&nbsp; <b>[ ✨ Features ]</b> &nbsp;•&nbsp; <b>[ 🛠️ Tech Stack ]</b> &nbsp;•&nbsp; <b>[ 📡 Server Setup ]</b>
</p>

---

</div>

<br />

## 📖 درباره برنامه | Overview

<div align="right" dir="rtl">

### 📌 ممـد پروکسـی چیست؟
**ممد پروکسی (Mamad Proxy)** یک اپلیکیشن بومی اندروید فوق‌العاده سریع، مدرن و روان است که با استفاده از **Jetpack Compose** و زبان **Kotlin** توسعه یافته است. این برنامه به طور اختصاصی برای **تست پینگ واقعی (Real-Time Ping)، مرتب‌سازی خودکار و اتصال آنلاین به پروکسی‌های MTProto تلگرام** طراحی شده است.

این برنامه لیست پروکسی‌های فعال را مستقیماً از فایل `index.txt` سرور اختصاصی دریافت کرده و با رابط کاربری منحصر‌به‌فرد **Liquid Glassmorphism (شیشه‌ای مایع)** و انیمیشن‌های فیزیکی بی‌نظیر به کاربر ارائه می‌دهد.

</div>

---

## 💎 ویژگی‌های شگفت‌انگیز | Key Features

<table>
  <tr>
    <td width="50%">
      <h3>💧 Liquid Glass UI</h3>
      <p>طراحی مدرن شیشه‌ای با افکت‌های ۳ بعدی، انباشتگی قطره‌ای (Liquid Lens Physics)، انعکاس نور و تم تاریک/روشن هوشمند.</p>
    </td>
    <td width="50%">
      <h3>📡 Live Server Fetching</h3>
      <p>دریافت خودکار پروکسی‌ها از فایل <code>index.txt</code> سرور بدون نیاز به بروزرسانی خود اپلیکیشن.</p>
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h3>⚡ Real-Time Ping Tester</h3>
      <p>تست تاخیر (Latency) لحظه‌ای سرورها با امکان تنظیم تایم‌اوت دستی (از ۵۰۰ تا ۲۰۰۰ میلی‌ثانیه).</p>
    </td>
    <td width="50%">
      <h3>🔄 Auto-Sorting Engine</h3>
      <p>مرتب‌سازی هوشمند پروکسی‌ها به ترتیب سرعت اتصال و آنلاین بودن در کمتر از چند ثانیه.</p>
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h3>📱 Floating Fluid Navigation</h3>
      <p>نوار ناوبری شناور با قابلیت جابه‌جایی دستی (Drag Gesture) و انیمیشن‌های پاسخ‌گوی فیزیکی (Spring Physics).</p>
    </td>
    <td width="50%">
      <h3>🌐 Native Bilingual (RTL/LTR)</h3>
      <p>پشتیبانی کامل و استاندارد از دو زبان <b>فارسی</b> و <b>انگلیسی</b> به همراه تغییر آنی جهت چیدمان.</p>
    </td>
  </tr>
  <tr>
    <td width="50%">
      <h3>📂 Local & File Imports</h3>
      <p>امکان وارد کردن فایل‌های سابسکریپشن متنی از حافظه گوشی با سیستم کنترل هوشمند فایل (Intent Manager).</p>
    </td>
    <td width="50%">
      <h3>⭐ Favorites & QR Sharing</h3>
      <p>ذخیره‌سازی سرورهای دلخواه، ساخت کد QR اختصاصی و اشتراک‌گذاری سریع لینک‌ها با دوستان.</p>
    </td>
  </tr>
</table>

---

## 🏗️ معماری و نحوه کارکرد | System Architecture

```mermaid
graph TD
    A[🌐 VPS Server / GitHub Pages] -->|Holds index.txt| B(⚡ Mamad Proxy App)
    B --> C{ViewModel Fetcher}
    C -->|Parse MTProto Links| D[📊 Ping Testing Engine]
    D -->|Calculate Latency| E[🔀 Auto-Sort Engine]
    E --> F[📱 Liquid Glass Compose UI]
    F -->|One-Tap Connect| G[✈️ Telegram App]
📡 راهنمای راه‌اندازی سرور اختصاصی | Server Setup (index.txt)
شما می‌توانید سرور اختصاصی خود را بسازید! کافیست فایل index.txt را روی VPS، Cloudflare Workers یا GitHub Pages میزبانی کنید.
📝 نمونه فرمت فایل index.txt:
code
Text
https://t.me/proxy?server=1.2.3.4&port=443&secret=ee123456789abcdef...
tg://proxy?server=proxy.example.com&port=8080&secret=7gAAAAAAAAAAAAAAAAAAAA...
🛠️ تکنولوژی‌های به‌کار رفته | Tech Stack
<p align="center">
<img src="https://img.shields.io/badge/Language-Kotlin_1.9-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/UI-Jetpack_Compose_Material3-4285F4?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Architecture-MVVM_+_Clean-00C853?style=for-the-badge" />
<img src="https://img.shields.io/badge/Async-Coroutines_%26_Flow-FF6D00?style=for-the-badge" />
<img src="https://img.shields.io/badge/Design-Glassmorphism_Shaders-D0BCFF?style=for-the-badge" />
</p>
📸 گالری تصاویر برنامه | App Screenshots
<div align="center">
<img src="https://raw.githubusercontent.com/modoto-mo/Mamad-Proxy/main/docs/screen_home.png" width="30%" alt="Home Screen" />
<img src="https://raw.githubusercontent.com/modoto-mo/Mamad-Proxy/main/docs/screen_local.png" width="30%" alt="Local Screen" />
<img src="https://raw.githubusercontent.com/modoto-mo/Mamad-Proxy/main/docs/screen_settings.png" width="30%" alt="Settings Screen" />
</div>
👑 سازنده و کانال پشتیبانی | Creator & Support
<div align="center">
👤 سازنده پروژه (Creator)	📢 کانال تلگرام (Support Channel)
❄️ Frozen Mamad ❄️	Mamad Config
@frzmmd	@mamad1vpn
<br />
![alt text](https://img.shields.io/badge/Join_Telegram_Channel-@mamad1vpn-26A5E4?style=for-the-badge&logo=telegram&logoColor=white)
</div>
<div align="center">
⭐ اگر این پروژه رو دوست داشتی، حتماً با زدن Star در بالای صفحه حمایتمون کن! ⭐
Crafted with ❤️ for Iranian Freedom & Internet Speed by modoto-mo
</div>
