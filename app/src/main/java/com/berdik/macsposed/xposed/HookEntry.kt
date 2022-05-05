package com.berdik.macsposed.xposed

import android.annotation.SuppressLint
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.berdik.macsposed.BuildConfig

import com.berdik.macsposed.xposed.location.WLANHooker
import java.lang.Exception

@ExperimentalStdlibApi
class HookEntry : IXposedHookZygoteInit, IXposedHookLoadPackage {
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)
    }

    @SuppressLint("PrivateApi", "ObsoleteSdkInt", "NewApi")
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam != null) {
            when (lpparam.packageName) {
                "android" -> {
                    EzXHelperInit.initHandleLoadPackage(lpparam)
                    EzXHelperInit.setLogTag("FuckLocation Xposed")
                    EzXHelperInit.setToastTag("FL")

                    try {
                        WLANHooker().hookWifiManager(lpparam)
                    } catch (e: Exception) {
                        XposedBridge.log("FL: fuck with exceptions: $e")
                    }
                }
            }
        }

    }
}