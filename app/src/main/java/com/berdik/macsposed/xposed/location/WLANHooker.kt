package com.berdik.macsposed.xposed.location

import android.annotation.SuppressLint
import android.net.MacAddress
import android.net.wifi.WifiConfiguration
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.kyuubiran.ezxhelper.utils.*
import dalvik.system.PathClassLoader
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WLANHooker {
    @RequiresApi(Build.VERSION_CODES.R)
    @ExperimentalStdlibApi
    @SuppressLint("PrivateApi")
    fun hookWifiManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz: Class<*> = lpparam.classLoader.loadClass("com.android.server.SystemServiceManager")
        XposedBridge.log("Searching for System Service Manager.")
        findAllMethods(clazz) {
            name == "loadClassFromLoader" && isPrivate && isStatic
        }.hookMethod {
            after { param ->
                if (param.args[0] == "com.android.server.wifi.WifiService") {
                    XposedBridge.log("System Service Manager found! Searching for WiFi Service.")
                    try {
                        val classloader = param.args[1] as PathClassLoader
                        val wifiClazz = classloader.loadClass("com.android.server.wifi.WifiConfigManager")

                        findAllMethods(wifiClazz) {
                            name == "getInternalConfiguredNetwork" && isPrivate
                        }.hookMethod {
                            after { param ->
                                if (param.result != null) {
                                    val mac = MacAddress.fromString("69:69:69:69:69:69");
                                    XposedHelpers.setObjectField(param.result, "mRandomizedMacAddress", mac)
                                    val config = param.result as WifiConfiguration
                                    XposedBridge.log("SSID is ${config.SSID}, MAC is ${config.randomizedMacAddress}")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        XposedBridge.log("FL: fuck with exceptions! $e")
                    }
                }
            }
        }
    }
}