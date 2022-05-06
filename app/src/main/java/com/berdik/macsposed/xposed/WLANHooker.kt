package com.berdik.macsposed.xposed

import android.annotation.SuppressLint
import android.net.MacAddress
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.kyuubiran.ezxhelper.utils.*
import dalvik.system.PathClassLoader
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WLANHooker {
    @RequiresApi(Build.VERSION_CODES.R)
    @ExperimentalStdlibApi
    @SuppressLint("PrivateApi")
    fun hookWifiManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        val clazz: Class<*> = lpparam.classLoader.loadClass("com.android.server.SystemServiceManager")
        findAllMethods(clazz) {
            name == "loadClassFromLoader" && isPrivate && isStatic
        }.hookMethod {
            after { param ->
                if (param.args[0] == "com.android.server.wifi.WifiService") {
                    try {
                        val classloader = param.args[1] as PathClassLoader

                        // Step 1
                        val wifiConfigManagerClass = classloader.loadClass("com.android.server.wifi.WifiConfigManager")
                        findAllMethods(wifiConfigManagerClass) {
                            name == "shouldUseEnhancedRandomization" && isPublic
                        }.hookReplace {
                            false
                        }

                        // Step 2
                        findAllMethods(wifiConfigManagerClass) {
                            name == "getPersistentMacAddress" && isPublic
                        }.hookReplace {
                            MacAddress.fromString("12:34:56:78:90:ab")
                            //MacAddress.fromString("50:1a:c5:cf:9f:8e") - Microsoft MAC
                        }

                        // Step 3
                        val wifiConfigClass = lpparam.classLoader.loadClass("android.net.wifi.WifiConfiguration")
                        findAllMethods(wifiConfigClass) {
                            name == "isValidMacAddressForRandomization" && isPublic
                        }.hookReplace {
                            true
                        }
                    } catch (e: Exception) {
                        XposedBridge.log("MACsposed Error: $e")
                    }
                }
            }
        }
    }
}

/*
    Step 1:
    https://cs.android.com/android/platform/superproject/+/master:packages/modules/Wifi/service/java/com/android/
        server/wifi/WifiConfigManager.java;drc=5e87eff15a543aa27ecaf00f41b3edc47de3fd6a;l=428

    Step 2:
    https://cs.android.com/android/platform/superproject/+/master:packages/modules/Wifi/service/java/com/android/
        server/wifi/WifiConfigManager.java;drc=5e87eff15a543aa27ecaf00f41b3edc47de3fd6a;l=505

    Step 3:
    https://cs.android.com/android/platform/superproject/+/master:packages/modules/Wifi/framework/java/android/
        net/wifi/WifiConfiguration.java;drc=5e87eff15a543aa27ecaf00f41b3edc47de3fd6a;l=1715
 */