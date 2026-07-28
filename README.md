باشه داداش، این یه **ریدمی حرفه‌ای و کامل** برای ریپازیتوریته که هم ساختار فنی داره، هم چشم‌نوازه. کافیه کپی کنی توی فایل `README.md` پروژه‌ات:

---

```markdown
# 🚀 ممد پروکسی (Mamad Proxy)

**ممد پروکسی** یک اپلیکیشن بومی اندروید است که با **Kotlin** و **Jetpack Compose** نوشته شده و وظیفه‌ی تست پینگ، مرتب‌سازی و مدیریت پروکسی‌های MTProto تلگرام را بر عهده دارد. لیست پروکسی‌ها به‌صورت خودکار از یک فایل `index.txt` روی سرور دریافت شده و بدون نیاز به آپدیت اپ، همیشه به‌روز می‌ماند.

---

## ✨ ویژگی‌های کلیدی

| ویژگی | توضیح |
|-------|-------|
| 📡 **دریافت خودکار از سرور** | پروکسی‌ها را از فایل `index.txt` روی هاست شما دریافت می‌کند. |
| ⚡ **تست پینگ لحظه‌ای** | تأخیر هر سرور را با تایم‌اوت قابل تنظیم (۵۰۰ تا ۲۰۰۰ میلی‌ثانیه) اندازه‌گیری می‌کند. |
| 🧠 **مرتب‌سازی هوشمند** | سرورها را بر اساس سرعت و وضعیت آنلاین بودن مرتب می‌سازد. |
| 🖼️ **Liquid Glass UI** | طراحی شیشه‌ای با افکت‌های سه‌بعدی، فیزیک لنز مایع و تم تاریک/روشن. |
| 🌐 **دو زبانه (فارسی/انگلیسی)** | پشتیبانی کامل از RTL و LTR با تغییر آنی زبان. |
| 📂 **وارد کردن فایل** | امکان وارد کردن فایل‌های سابسکریپشن از حافظه‌ی گوشی. |
| ⭐ **ذخیره علاقه‌مندی‌ها** | نشانه‌گذاری سرورهای دلخواه و ساخت کد QR برای اشتراک‌گذاری. |
| 🧭 **ناوبری شناور** | نوار ناوبری با قابلیت جابه‌جایی دستی و انیمیشن‌های فیزیکی. |

---

## 📥 نحوه‌ی دریافت پروکسی‌ها

اپلیکیشن لیست پروکسی‌ها را از آدرسی که در کد تنظیم شده (پیش‌فرض: سرور اختصاصی سازنده) دریافت می‌کند. شما می‌توانید سرور خود را نیز راه‌اندازی کنید.

### راه‌اندازی سرور اختصاصی

۱. یک فایل `index.txt` با فرمت زیر بسازید:
```
https://t.me/proxy?server=1.2.3.4&port=443&secret=ee123456789abcdef...
tg://proxy?server=proxy.example.com&port=8080&secret=7gAAAAAAAAAAAAAAAAAAAA...
```
۲. فایل را روی **VPS**، **Cloudflare Workers** یا **GitHub Pages** میزبانی کنید.
۳. آدرس سرور را در کد اپ (یا در بخش تنظیمات) تغییر دهید.

---

## 🛠️ تکنولوژی‌های استفاده‌شده

- **زبان:** Kotlin
- **UI Framework:** Jetpack Compose
- **معماری:** MVVM با ViewModel
- **مدیریت شبکه:** OkHttp + Coroutines
- **ذخیره‌سازی محلی:** DataStore Preferences

---

## 📱 نصب و اجرا

۱. ریپازیتوری را کلون کنید:
```bash
git clone https://github.com/modoto-mo/Mamad-Proxy.git
```
۲. پروژه را در **Android Studio** باز کنید.
۳. منتظر بمانید تا Gradle همگام‌سازی شود.
۴. اپ را روی دستگاه یا شبیه‌ساز اجرا کنید.

---

## 🧪 ساخت نسخه‌ی نهایی (APK)

برای ساخت فایل APK نهایی:
```bash
./gradlew assembleRelease
```
فایل خروجی در `app/build/outputs/apk/release/` قرار می‌گیرد.

---

## 🤝 مشارکت در توسعه

اگر ایده، باگ یا پیشنهادی دارید، خوشحال می‌شویم که **Pull Request** یا **Issue** باز کنید. 

---

## 👨‍💻 سازنده و پشتیبانی

- **❄️ Frozen Mamad** ([@frzmmd](https://t.me/frzmmd))
- کانال پشتیبانی: [@mamad1vpn](https://t.me/mamad1vpn)

---

## 📜 مجوز

این پروژه تحت مجوز **MIT** منتشر شده است. برای جزئیات بیشتر فایل `LICENSE` را ببینید.

---

**⚠️ نکته امنیتی:** لطفاً تنها از سرورهای معتبر استفاده کنید و پروکسی‌های ناشناس را با احتیاط به کار ببرید.

---

> با ❤️ از ایران
```

---

## 🌐 نسخه‌ی انگلیسی (سریع)

If you prefer English:

```markdown
# 🚀 Mamad Proxy

**Mamad Proxy** is a native Android app built with **Kotlin** and **Jetpack Compose** that pings, sorts, and manages MTProto Telegram proxies. It fetches the proxy list from a remote `index.txt` file, so no app updates are needed.

## Key Features
- Auto-fetch from server
- Real-time ping test (500–2000ms timeout)
- Smart sorting by speed & availability
- Liquid Glass UI (dark/light themes)
- Persian/English support (RTL & LTR)
- Import from local storage
- Favorites & QR code sharing
- Floating navigation with physics

## Setup Your Own Server
Create an `index.txt` with lines like:
```
https://t.me/proxy?server=1.2.3.4&port=443&secret=...
tg://proxy?server=proxy.example.com&port=8080&secret=...
```
Host it on VPS, Cloudflare Workers, or GitHub Pages.

## Tech Stack
- Kotlin + Jetpack Compose
- MVVM + ViewModel
- OkHttp + Coroutines
- DataStore Preferences

## Build & Run
```bash
git clone https://github.com/modoto-mo/Mamad-Proxy.git
# Open in Android Studio, sync Gradle, run on device/emulator
```

## Build APK
```bash
./gradlew assembleRelease
```

## Contribute
Issues and PRs are welcome!

## Author & Support
- **❄️ Frozen Mamad** ([@frzmmd](https://t.me/frzmmd))
- Channel: [@mamad1vpn](https://t.me/mamad1vpn)

## License
MIT
```

---

این ریدمی رو بذار تو ریپو، دیگه هیچکس بهت گیر نمیده! 😎👌
