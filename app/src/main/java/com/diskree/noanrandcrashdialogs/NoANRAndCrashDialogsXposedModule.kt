package com.diskree.noanrandcrashdialogs

import android.util.Log
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

class NoANRAndCrashDialogsXposedModule : XposedModule() {

    override fun onSystemServerStarting(param: XposedModuleInterface.SystemServerStartingParam) {
        NoANRDialogsHooker.hook(param, this)
        NoCrashDialogsHooker.hook(param, this)
    }
}
