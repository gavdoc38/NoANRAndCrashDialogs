<p align="center">
  <img src="https://img.shields.io/badge/Android-8.1_–_16%2B-34D058?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Vector_2.0-FF6F00?style=for-the-badge&logo=github&logoColor=white" alt="Vector 2.0"/>
  <img src="https://img.shields.io/badge/libxposed_API_101-8A2BE2?style=for-the-badge&logo=github&logoColor=white" alt="libxposed API 101"/>
  <img src="https://img.shields.io/badge/License-CC0_1.0-lightgrey?style=for-the-badge" alt="License"/>
</p>

<h1 align="center">
  🛡 NoANRAndCrashDialogs
</h1>

<p align="center">
  <b>Silence the noise.</b> Automatically dismiss system ANR and crash dialogs — silently, instantly, every time.
</p>

<p align="center">
  <i>Forked and modernized from <a href="https://github.com/diskree/NoANRAndCrashDialogs">diskree/NoANRAndCrashDialogs</a></i>
</p>

---

## ✨ What It Does

| Dialog | Behaviour |
|---|---|
| **ANR** (App Not Responding) | API 31+: auto‑sends `WAIT` command → dialog dismissed but ANR still logged. API 27–30: hook skipped entirely. |
| **Crash** (App has stopped) | Hooks `showCrashDialogs()` / `AppErrors` → silently returns. Zero popups. |

---

## ✅ Tested On

| Device | Android | Framework | Result |
|---|---|---|---|
| 🔥 Xiaomi Poco F4 GT *(ingres)* | **16** (API 36) | **Vector 2.0** (3043) | ✅ Loaded & active |

> *Should run on any device with **Android 8.1 – 16+** and a **libxposed 101‑compatible** framework (Vector 2.0 recommended).*

---

## 📦 Requirements

- 🧩 **Vector 2.0** framework (or any libxposed‑101 backend)
- 🔓 Rooted device — Magisk / KernelSU with Zygisk enabled

---

## 🔧 Build

```bash
git clone https://github.com/cahjul/NoANRAndCrashDialogs.git
cd NoANRAndCrashDialogs
./gradlew assembleRelease
```

> APK → `app/build/outputs/apk/release/app-release.apk`

A prebuilt APK is included in the repo root and in [Releases](https://github.com/cahjul/NoANRAndCrashDialogs/releases).

---

## 📲 Install

```bash
# 1. Install the APK
adb install app-release.apk

# 2. Enable via Vector CLI (or the Vector manager app)
adb shell su -c '/data/adb/modules/zygisk_vector/cli modules enable com.diskree.noanrandcrashdialogs'

# 3. Set scope to system_server
adb shell su -c "sqlite3 /data/adb/lspd/config/modules_config.db \
  \"INSERT OR IGNORE INTO scope (mid, app_pkg_name, user_id) \
   SELECT mid, 'system', 0 FROM modules \
   WHERE module_pkg_name='com.diskree.noanrandcrashdialogs';\""

# 4. Reboot
adb reboot
```

---

## 🐛 Bug Reports

Open an [issue](https://github.com/cahjul/NoANRAndCrashDialogs/issues) and include:

```
1. Device model & Android version
   adb shell getprop ro.product.model
   adb shell getprop ro.build.version.sdk

2. Vector version
   adb shell cat /data/adb/modules/zygisk_vector/module.prop | grep version

3. Module log
   adb shell su -c 'cat /data/adb/lspd/log/modules_*.log'

4. Verbose log (filtered)
   adb shell su -c 'cat /data/adb/lspd/log/verbose_*.log | grep -i noanr'

5. What happened? What did you expect?
```

---

## 📋 Changelog

### v2.0 – Vector 2.0 Edition
| Before | After |
|---|---|
| ❌ LSPosed only | ✅ **Vector 2.0** framework |
| ❌ Android 15 only | ✅ **Android 8.1 → 16+** (API 27–36) |
| ❌ libxposed API 100 *(unpublished)* | ✅ **libxposed API 101.0.1** *(published)* |
| ❌ AGP 8.8.0 / Kotlin 1.9.24 | ✅ **AGP 8.10.0 / Kotlin 2.1.0** |
| ❌ Hooks hardcoded for A15 | ✅ **Version‑aware** — `ErrorDialogController` (31+) / `AppErrors` (27–30) |

---

## 🙏 Credits

- **[diskree](https://github.com/diskree/NoANRAndCrashDialogs)** — original author (CC0 1.0)
- **[JingMatrix / Vector](https://github.com/JingMatrix/Vector)** — Vector 2.0 framework
- **[libxposed](https://github.com/libxposed/api)** — modern Xposed API

<p align="center">
  <sub>Built with assistance from <b>DeepSeek V4 Flash</b> 🤖</sub>
</p>

---

## ⚖ License

[CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/legalcode) — see [LICENSE](LICENSE).  
Same as the original project.
