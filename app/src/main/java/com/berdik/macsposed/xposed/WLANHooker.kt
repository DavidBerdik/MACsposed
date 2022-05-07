package com.berdik.macsposed.xposed

import android.annotation.SuppressLint
import android.net.MacAddress
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.kyuubiran.ezxhelper.utils.*
import dalvik.system.PathClassLoader
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.net.NetworkInterface

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

                        // Step 5
                        val wifiVendorHalClass = classloader.loadClass("com.android.server.wifi.WifiVendorHal")
                        findAllMethods(wifiVendorHalClass) {
                            name == "setStaMacAddress" && isPublic
                        }.hookMethod {
                            before { param ->
                                XposedBridge.log("BEFORE setStaMacAddress: ${MacAddress.fromBytes(NetworkInterface.getByName("wlan0").hardwareAddress)}")
                                param.result = true
                            }
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
    Step 5:
    https://cs.android.com/android/platform/superproject/+/master:packages/modules/Wifi/service/java/com/android/
        server/wifi/WifiVendorHal.java;drc=8edc7df508e04e982b9ef715d2f48e5cb3b1478e;l=1730
 */