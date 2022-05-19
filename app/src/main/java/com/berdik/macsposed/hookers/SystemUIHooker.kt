package com.berdik.macsposed.hookers

import android.annotation.SuppressLint
import android.util.ArraySet
import com.berdik.macsposed.BuildConfig
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SystemUIHooker {
    companion object {
        private const val tileId = "custom(${BuildConfig.APPLICATION_ID}/.QuickTile)"
        private var tileAdded = false
        private var tileRevealed = false

        @SuppressLint("PrivateApi")
        fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.QSPanelControllerBase")) {
                name == "setTiles" && isPublic && paramCount == 0
            }.hookMethod {
                before { param ->
                    if (!tileAdded) {
                        val tileHost = XposedHelpers.getObjectField(param.thisObject, "mHost")
                        XposedHelpers.callMethod(tileHost, "addTile", tileId, -1)
                        XposedBridge.log("[MACsposed] Tile added to quick settings panel.")
                        tileAdded = true
                    }
                }
            }

            // Properly fixing the unchecked cast warning with Kotlin adds more performance overhead than it is worth,
            // so we are suppressing the warning instead.
            @Suppress("UNCHECKED_CAST")
            findAllMethods(lpparam.classLoader.loadClass("com.android.systemui.qs.PagedTileLayout")) {
                name == "startTileReveal" && isPublic
            }.hookMethod {
                before { param ->
                    if (!tileRevealed) {
                        val tilesToReveal = param.args[0] as ArraySet<String>
                        tilesToReveal.add(tileId)
                        XposedBridge.log("[MACsposed] Tile reveal animation played.")
                        tileRevealed = true
                    }
                }
            }
        }
    }
}