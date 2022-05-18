package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import com.github.kyuubiran.ezxhelper.utils.findAllMethods
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.github.kyuubiran.ezxhelper.utils.isPublic
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SystemUIHooker {
    companion object {
        @SuppressLint("PrivateApi")
        fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSTileRevealController")) {
                name == "setExpansion" && isPublic
            }.hookMethod {
                before { param ->
                    if (param.args[0] == 1f)
                        XposedBridge.log("[MACsposed] PANEL OPENED!")
                }
            }
        }
    }
}