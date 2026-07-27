<div align="center">

# ⚡ Mamad Proxy - Telegram Proxy Speed & Ping Tester

### *Ultra-Modern, Liquid-Glassmorphic Android App for Testing & Connecting to Telegram MTProto Proxies*

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin_1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![UI](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Telegram](https://img.shields.io/badge/Channel-@mamad1vpn-26A5E4?style=for-the-badge&logo=telegram&logoColor=white)](https://t.me/mamad1vpn)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

<br />

**[English](#-about-the-project) • [فارسی](#-درباره-برنامه) • [Features](#-features--ویژگی‌ها) • [Tech Stack](#-tech-stack) • [Installation](#-build--installation)**

---

</div>

<br />

## 🌟 درباره برنامه | About the Project

<p dir="rtl" align="right">
<b>ممد پروکسی (Mamad Proxy)</b> یک اپلیکیشن مدرن، روان و فوق‌العاده زیبا برای اندروید است که جهت <b>تست سرعت، پینگ واقعی، مرتب‌سازی و اتصال مستقیم به پروکسی‌های تلگرام (MTProto)</b> طراحی شده است. این برنامه با بهره‌گیری از جدیدترین فریمورک گوگل (Jetpack Compose) و طراحی اختصاصی <b>Liquid Glassmorphism (شیشه‌ای مایع)</b>، تجربه‌ای بی‌نظیر با انیمیشن‌های فیزیکی روان به شما ارائه می‌دهد.
</p>

---

## 🔥 ویژگی‌ها | Features

- 💧 **رابط کاربری شیشه‌ای مایع (Liquid Glassmorphism):** کارت‌ها و دکمه‌های فوق‌العاده شیک با انعکاس نور، تم تاریک/روشن هوشمند و افکت‌های ۳ بعدی.
- 🧪 **تست سرعت و پینگ واقعی (Real-time Ping Test):** اندازه‌گیری تاخیر (Latency) پروکسی‌ها با امکان تنظیم تایم‌اوت دستی (Proxy Timeout MS).
- 🔀 **مرتب‌سازی خودکار (Auto-Sort):** مرتب کردن هوشمند لیست پروکسی‌ها بر اساس آنلاین بودن و سریع‌ترین پینگ.
- 🎯 **اتصال مستقیم با یک کلیک (Direct Connect):** باز کردن مستقیم لینک‌های MTProto در اپلیکیشن تلگرام.
- 📱 **منوی شناور فیزیکی (Floating Glass Lens Navigation):** نوار پایینی شناور با قابلیت جابه‌جایی دستی (Drag Gesture) و فیزیک انباشتگی قطره‌ای.
- 🌐 **پشتیبانی دو زبانه (Bilingual RTL / LTR):** تغییر آنی زبان بین **فارسی** و **انگلیسی** با چیدمان استاندارد.
- 📂 **وارد کردن سابسکریپشن از فایل (Subscription Import):** قابلیت خواندن مستقیم سابسکریپشن‌های پروکسی از طریق باز کردن فایل‌ها در گوشی.
- ⭐ **لیست علاقه‌مندی‌ها (Saved Proxies):** ذخیره‌سازی پروکسی‌های محبوب و امکان تست سرعت مجزا برای آن‌ها.
- 🔍 **فیلتر و جستجوی پیشرفته:** جستجوی آنی بر اساس پورت یا نام سرور و فیلتر بر اساس (همه، آنلاین، سریع‌ترین‌ها).
- 📲 **تولید کد QR:** نمایش و اسکن سریع لینک‌های پروکسی جهت اشتراک‌گذاری با دوستان.

---

## 🛠️ تکنولوژی‌های استفاده شده | Tech Stack

- **زبان برنامه نویسی:** Kotlin
- **فریمورک رابط کاربری:** Jetpack Compose (Material 3)
- **معماری:** MVVM (Model-View-ViewModel) + StateFlow & Coroutines
- **طراحی گرافیکی:** Custom Canvas Shaders, Radial Gradients & Specular Reflections
- **انیمیشن‌ها:** Compose Animation, Infinite Transition, Spring Physics, Gesture Drag Detectors

---

## 📂 ساختار منوها و بخش‌های برنامه

```text
Mamad Proxy App
├── 🏠 Home (خانه)       -> مشاهده، جستجو، فیلتر و تست سرعت پروکسی‌ها
├── 📁 Local (لوکال)      -> مدیریت پروکسی‌های محلی و سابسکریپشن‌های وارد شده
└── ⚙️ Settings (تنظیمات) -> تغییر پوسته، زبان، تایم‌اوت پروکسی و پروکسی‌های ذخیره‌شده
