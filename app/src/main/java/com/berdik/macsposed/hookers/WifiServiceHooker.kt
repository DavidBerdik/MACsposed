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
                name == "loadClassFromLoader" && isStatic
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
            val wifiVendorHalClass = classloader.loadClass("com.android.server.wifi.WifiVendorHal")
            macAddrSetGenericHook(wifiVendorHalClass, "setStaMacAddress")
            macAddrSetGenericHook(wifiVendorHalClass, "setApMacAddress")
        }

        private fun macAddrSetGenericHook(wifiVendorHalClass: Class<*>, functionName: String) {
            findAllMethods(wifiVendorHalClass) {
                name == functionName
            }.hookMethod {
                var isHookActive = false

                before { param ->
                    // Get the active state of the hook.
                    val prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID)
                    isHookActive = prefs.getBoolean("hookActive", false)

                    // If the hook is active, log a block of the MAC address change and bypass the real function.
                    if (isHookActive) {
                        XposedBridge.log("[MACsposed] Blocked MAC address change to ${param.args[1]} on ${param.args[0]}.")
                        param.result = true
                    }
                }

                after { param ->
                    // If the hook is active and the result of the address change attempt was successful, make a log entry
                    // after the real function executes indicating so.
                    if (param.result as Boolean && !isHookActive) {
                        XposedBridge.log("[MACsposed] Allowed MAC address change to ${param.args[1]} on ${param.args[0]}.")
                    }
                }
            }
        }
    }
}