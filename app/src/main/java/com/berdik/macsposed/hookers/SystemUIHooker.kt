package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SystemUIHooker {
    companion object {
        @SuppressLint("PrivateApi")
        fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSPanelControllerBase")) {
                name == "setRevealExpansion" && isPublic
            }.hookMethod {
                before { param ->
                    if (param.args[0] == 1f) {
                        val tileHost = XposedHelpers.getObjectField(param.thisObject, "mHost")
                        XposedHelpers.callMethod(tileHost, "addTile", "custom(com.berdik.macsposed/.QuickTile)", -1)
                        XposedBridge.log("[MACsposed] PANEL OPENED!")
                    }
                }
            }

            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSTileHost")) {
                name == "addTile" && isPublic && paramCount == 2
            }.hookMethod {
                before { param ->
                    XposedBridge.log("[MACsposed] addTile CALLED - ${param.args[0]}")
                }
            }

            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSTileHost")) {
                name == "changeTiles" && isPublic && paramCount == 2
            }.hookMethod {
                before { param ->
                    XposedBridge.log("[MACsposed] changeTiles CALLED - ${param.args[0]} | ${param.args[1]}")
                }
            }
        }
    }
}