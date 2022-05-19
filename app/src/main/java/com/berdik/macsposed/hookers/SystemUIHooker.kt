package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import android.util.ArraySet
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
                        val tileSpecs = XposedHelpers.getObjectField(tileHost, "mTileSpecs") as ArrayList<String>
                        XposedBridge.log("[MACsposed] PANEL OPENED! - tileSpecs $tileSpecs")
                    }
                }
            }

            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSTileRevealController")) {
                name == "setExpansion" && isPublic
            }.hookMethod {
                before { param ->
                    if (param.args[0] == 1f) {
                        val tilesToReveal = XposedHelpers.getObjectField(param.thisObject, "mTilesToReveal") as ArraySet<String>
                        tilesToReveal.add("custom(com.berdik.macsposed/.QuickTile)")
                        XposedHelpers.setObjectField(param.thisObject, "mTilesToReveal", tilesToReveal)
                        XposedBridge.log("[MACsposed] Hooked setExpansion in TileRevealController - ${tilesToReveal}")
                    }
                }
            }

            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.PagedTileLayout")) {
                name == "startTileReveal" && isPublic
            }.hookMethod {
                before { param ->
                    XposedBridge.log("[MACsposed] Hooked startTileReveal in PagedTileLayout, ${param.args[0]}; ${param.args[1]}")
                }
            }
        }
    }
}