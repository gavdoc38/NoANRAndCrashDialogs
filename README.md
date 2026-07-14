# NoANRAndCrashDialogs

A libxposed module (compatible with **Vector 2.0**) that automatically hides or dismisses system ANR ("App Not Responding") and crash ("App has stopped") dialogs.

Forked and updated from [diskree/NoANRAndCrashDialogs](https://github.com/diskree/NoANRAndCrashDialogs).

## Changes from original

- **Vector 2.0** framework support (replaces LSPosed)
- **Android 8.1 – 16+** compatibility (API 27–36), up from Android 15 only
- Updated to **libxposed API 101** (the original used unpublished API 100)
- Modern AGP 8.10.0 + Kotlin 2.1.0 toolchain
- Version‑aware hooks: `ErrorDialogController` (API 31+) and `AppErrors` fallback (API 27–30)

## Requirements

- **Vector 2.0** (or another libxposed‑compatible framework)
- Rooted device (Magisk / KernelSU with Zygisk)

## Build

```bash
git clone https://github.com/diskria-android/NoANRAndCrashDialogs.git
cd NoANRAndCrashDialogs
./gradlew assembleRelease
```

APK is at `app/build/outputs/apk/release/app-release.apk`.

## Install

1. Install the APK
2. Open the Vector manager app
3. Enable the module and set scope to **system**
4. Reboot

## Credits

- **[diskree](https://github.com/diskree/NoANRAndCrashDialogs)** — original creator of this module (CC0 1.0 license)
- **[JingMatrix](https://github.com/JingMatrix/Vector)** — Vector 2.0 framework
- **[libxposed](https://github.com/libxposed/api)** — modern Xposed API

*This codebase was updated with assistance from **DeepSeek V4 Flash**.*

## License

[CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/legalcode) — see [LICENSE](LICENSE).  
Same as the original project.
