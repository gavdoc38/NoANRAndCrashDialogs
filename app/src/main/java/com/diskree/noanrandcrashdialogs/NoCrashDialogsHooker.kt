package com.diskree.noanrandcrashdialogs

import android.annotation.SuppressLint
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedInterface.Chain
import io.github.libxposed.api.XposedInterface.Hooker
import io.github.libxposed.api.XposedModuleInterface

@SuppressLint("PrivateApi")
object NoCrashDialogsHooker {

    private const val TAG = "NoANRAndCrashDialogs"
    private const val DIALOG_DATA_CLASS_NAME = "com.android.server.am.AppErrorDialog\$Data"

    fun hook(param: XposedModuleInterface.SystemServerStartingParam, module: XposedInterface) {
        val cl = param.classLoader

        if (hookV31Plus(cl, module)) return
        if (hookV27to30(cl, module)) return

        module.log(Log.WARN, TAG, "No crash hook method found for this Android version")
    }

    private fun hookV31Plus(cl: ClassLoader, module: XposedInterface): Boolean {
        return try {
            val controllerClass = cl.loadClass("com.android.server.am.ErrorDialogController")
            val showMethod = controllerClass.getDeclaredMethod(
                "showCrashDialogs",
                cl.loadClass(DIALOG_DATA_CLASS_NAME)
            ).accessed()
            module.hook(showMethod).intercept(CrashDialogShowingHooker())
            true
        } catch (_: Throwable) {
            false
        }
    }

    private fun hookV27to30(cl: ClassLoader, module: XposedInterface): Boolean {
        return try {
            val appErrorsClass = cl.loadClass("com.android.server.am.AppErrors")
            val processRecordClass = cl.loadClass("com.android.server.am.ProcessRecord")

            try {
                val method = appErrorsClass.getDeclaredMethod(
                    "handleShowErrorDialog",
                    processRecordClass, Int::class.javaPrimitiveType!!,
                    String::class.java, String::class.java,
                    Long::class.javaPrimitiveType!!, String::class.java, Int::class.javaPrimitiveType!!
                ).accessed()
                module.hook(method).intercept(LegacyCrashHooker())
                return true
            } catch (_: NoSuchMethodException) { }

            try {
                val method = appErrorsClass.getDeclaredMethod(
                    "crashApplication",
                    Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, processRecordClass,
                    String::class.java, Int::class.javaPrimitiveType!!, String::class.java,
                    String::class.java, String::class.java, Long::class.javaPrimitiveType!!
                ).accessed()
                module.hook(method).intercept(LegacyCrashHooker())
                return true
            } catch (_: NoSuchMethodException) { }

            false
        } catch (_: Throwable) {
            false
        }
    }

    private class CrashDialogShowingHooker : Hooker {

        override fun intercept(chain: Chain): Any? {
            return null
        }
    }

    private class LegacyCrashHooker : Hooker {

        override fun intercept(chain: Chain): Any? {
            return null
        }
    }
}
