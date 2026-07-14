package com.diskree.noanrandcrashdialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import io.github.libxposed.api.XposedInterface
import io.github.libxposed.api.XposedModuleInterface
import io.github.libxposed.api.XposedInterface.Chain
import io.github.libxposed.api.XposedInterface.Hooker

@SuppressLint("PrivateApi")
object NoANRDialogsHooker {

    private const val TAG = "NoANRAndCrashDialogs"
    private const val DIALOG_DATA_CLASS_NAME = "com.android.server.am.AppNotRespondingDialog\$Data"
    private const val WAIT_COMMAND_CODE = 2

    fun hook(param: XposedModuleInterface.SystemServerStartingParam, module: XposedInterface) {
        val cl = param.classLoader

        if (hookV31Plus(cl, module)) return
        if (hookV27to30(cl, module)) return

        module.log(Log.WARN, TAG, "No ANR hook method found for this Android version")
    }

    private fun hookV31Plus(cl: ClassLoader, module: XposedInterface): Boolean {
        return try {
            val controllerClass = cl.loadClass("com.android.server.am.ErrorDialogController")
            val showMethod = controllerClass.getDeclaredMethod(
                "showAnrDialogs",
                cl.loadClass(DIALOG_DATA_CLASS_NAME)
            ).accessed()
            module.hook(showMethod).intercept(ANRDialogShowingHooker())
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
                    "appNotResponding",
                    String::class.java, Int::class.javaPrimitiveType!!, processRecordClass
                ).accessed()
                module.hook(method).intercept(LegacyANRHooker())
                return true
            } catch (_: NoSuchMethodException) { }

            try {
                val method = appErrorsClass.getDeclaredMethod(
                    "appNotResponding",
                    String::class.java, Int::class.javaPrimitiveType!!, processRecordClass,
                    String::class.java
                ).accessed()
                module.hook(method).intercept(LegacyANRHooker())
                return true
            } catch (_: NoSuchMethodException) { }

            false
        } catch (_: Throwable) {
            false
        }
    }

    private class ANRDialogShowingHooker : Hooker {

        override fun intercept(chain: Chain): Any? {
            try {
                val controller = chain.thisObject
                if (controller == null) return null

                val isSilentANR: Boolean? = controller
                    .getField<Any>("mApp")
                    ?.getField<Any>("mErrorState")
                    ?.invokeMethod("isSilentAnr")
                if (isSilentANR == null) return null

                val contexts: List<Context>? = controller.invokeMethod(
                    "getDisplayContexts",
                    arrayOf(Boolean::class.javaPrimitiveType!!),
                    isSilentANR
                )
                if (contexts.isNullOrEmpty()) return null

                val service: Any? = controller.getField("mService")
                if (service == null) return null

                val data = chain.getArg(0)
                val classLoader = data.javaClass.classLoader
                if (classLoader == null) return null

                val dialogClass =
                    classLoader.loadClass("com.android.server.am.AppNotRespondingDialog")
                val activityManagerServiceClass = classLoader
                    .loadClass("com.android.server.am.ActivityManagerService")
                val dialogDataClass = classLoader.loadClass(DIALOG_DATA_CLASS_NAME)
                for (context in contexts) {
                    try {
                        val dialog = dialogClass.newInstance(
                            arrayOf(
                                activityManagerServiceClass,
                                Context::class.java,
                                dialogDataClass
                            ),
                            service,
                            context,
                            data
                        ) ?: continue

                        val handler = dialog!!.getField<Handler>("mHandler") ?: continue
                        handler.obtainMessage(WAIT_COMMAND_CODE).sendToTarget()
                    } catch (_: Throwable) {
                        continue
                    }
                }
                return null
            } catch (_: Throwable) {
                return null
            }
        }
    }

    private class LegacyANRHooker : Hooker {

        override fun intercept(chain: Chain): Any? {
            return null
        }
    }
}
