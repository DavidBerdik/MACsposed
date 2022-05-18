package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import com.berdik.macsposed.BuildConfig
import com.github.kyuubiran.ezxhelper.utils.*
import dalvik.system.PathClassLoader
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WifiServiceHooker {
    companion object {
        @SuppressLint("PrivateApi")
        fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
            findAllMethods(lpparam.classLoader.loadClass("com.android.server.SystemServiceManager")) {
                name == "loadClassFromLoader" && isPrivate && isStatic
            }.hookMethod {
                after { param ->
                    if (param.args[0] == "com.android.server.wifi.WifiService") {
                        hookMacAddrSet(param.args[1] as PathClassLoader)
                    }
                }
            }
        }

        @SuppressLint("PrivateApi")
        private fun hookMacAddrSet(classloader: PathClassLoader) {
            findAllMethods(classloader.loadClass("com.android.server.wifi.WifiVendorHal")) {
                name == "setStaMacAddress" && isPublic
            }.hookMethod {
                before { param ->
                    val prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID)
                    if (prefs.getBoolean("hookActive", false)) {
                        XposedBridge.log("[MACsposed] Blocked MAC address change to ${param.args[1]} on ${param.args[0]}.")
                        param.result = true
                    }
                }

                after { param ->
                    val prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID)
                    if (param.result as Boolean && !prefs.getBoolean("hookActive", false)) {
                        XposedBridge.log("[MACsposed] Allowed MAC address change to ${param.args[1]} on ${param.args[0]}.")
                    }
                }
            }
        }
    }
}