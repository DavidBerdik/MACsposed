package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import android.util.ArraySet
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SystemUIHooker {
    companion object {
        val tileId = "custom(com.berdik.macsposed/.QuickTile)"

        @SuppressLint("PrivateApi")
        fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSPanelControllerBase")) {
                name == "setRevealExpansion" && isPublic
            }.hookMethod {
                before { param ->
                    if (param.args[0] == 1f) {
                        val tileHost = XposedHelpers.getObjectField(param.thisObject, "mHost")
                        XposedHelpers.callMethod(tileHost, "addTile", tileId, -1)
                        XposedBridge.log("[MACsposed] PANEL OPENED!")
                    }
                }
            }

            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.PagedTileLayout")) {
                name == "startTileReveal" && isPublic
            }.hookMethod {
                before { param ->
                    val tilesToReveal = param.args[0] as ArraySet<String>
                    tilesToReveal.add(tileId)
                    XposedBridge.log("[MACsposed] Hooked startTileReveal in PagedTileLayout, ${param.args[0]}")
                }
            }
        }
    }
}